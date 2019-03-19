import algorithms.OffLattice;
import com.google.devtools.common.options.OptionsParser;
import io.OvitoWriter;
import io.Parser;
import io.SimulationOptions;
import models.Particle;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class App {

	public static void main(String[] args) {
		// Parse command line options
		OptionsParser parser = OptionsParser.newOptionsParser(SimulationOptions.class);
		parser.parseAndExitUponError(args);
		SimulationOptions options = parser.getOptions(SimulationOptions.class);
		assert options != null;
		if (options.rc < 0
				|| options.noise < 0
				|| options.boxSide < 0
				|| options.speed < 0
				|| options.time <= 0
				|| options.dynamicFile.isEmpty()) {
			printUsage(parser);
		}

		// Parse dynamic file
		Parser dynamicParser = new Parser(options.dynamicFile);
		if (!dynamicParser.parse()) return;

		Queue<Particle> particles = dynamicParser.getParticles();

		// Validate matrix size meets non-punctual criteria
		if (!BoxSizeMeetsCriteria(options.M,
				options.boxSide,
				options.rc)) {
			System.out.println("L / M > interactionRadius failed.");
			return;
		}

		// Run algorithm
		runAlgorithm(particles,
				options.boxSide,
				options.M,
				options.rc);
	}

	private static void runAlgorithm(Queue<Particle> particles,
	                                 double L,
	                                 int M,
	                                 double interactionRadius) {

		long startTime = System.currentTimeMillis();

		OffLattice.run(particles,
				L,
				M,
				interactionRadius);

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		System.out.println("Cell Index Method execution time: " + elapsedTime + "ms");

		for (Particle p : particles) {
			System.out.print(p.getId());
			for (Particle neighbour : p.getNeighbours()) {
				System.out.print(" " + neighbour.getId());
			}
			System.out.print("\n");
		}

		OvitoWriter<Particle> ovitoWriter;
		try {
			ovitoWriter = new OvitoWriter<>(Paths.get("ovito_file.txt"));
			ovitoWriter.exportParticles(new LinkedList<>(particles), 0);
			ovitoWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void printUsage(OptionsParser parser) {
		System.out.println("Usage: java -jar tp2-1.0-SNAPSHOT.jar OPTIONS");
		System.out.println(parser.describeOptions(Collections.emptyMap(),
				OptionsParser.HelpVerbosity.LONG));
	}

	private static boolean BoxSizeMeetsCriteria(int M, double L, double interactionRadius) {
		return M > 0 && L / M > interactionRadius;
	}
}
