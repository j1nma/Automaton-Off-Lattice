package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Queue;

public class OctaveWriter {

	private final File file;
	private final FileWriter fileWriter;

	public OctaveWriter(File file) throws IOException {
		Optional<File> containingDir = Optional.ofNullable(file.getParentFile());
		if (containingDir.map(dir -> !dir.exists() && !dir.mkdirs()).orElse(false)) {
			throw new IllegalStateException("Could not create dir: " + containingDir);
		}
		this.file = file;
		this.fileWriter = new FileWriter(this.file);
	}

	public OctaveWriter(Path path) throws IOException {
		this(path.toFile());
	}

	public void close() throws IOException {
		this.fileWriter.close();
	}

	public void writeOrderValuesThroughIterations(final Queue<Double> orderValues) throws IOException {
		final StringBuilder builder = new StringBuilder();
		builder.append("result = [").append(Double.toString(orderValues.poll()));
		while (!orderValues.isEmpty()) {
			builder.append(", ").append(Double.toString(orderValues.poll()));
		}
		builder.append("];").append("\n")
				.append("plot(result);").append("\n")
				.append("xlabel(\"Iterations\");").append("\n")
				.append("ylabel(\"Va\");").append("\n");
		fileWriter.append(builder.toString());
		fileWriter.flush();
	}
}
