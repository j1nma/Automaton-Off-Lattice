package algorithms;

import models.Particle;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * Off-Lattice implementation
 */
public class OffLattice {

	/**
	 * Cells from 0 to MxM - 1.
	 * Each one has a list of CellParticles from that cell number.
	 * A CellParticle contains a Particle and the cell's position.
	 */
	private static List<List<CellParticle>> cells = new ArrayList<>();
	private static double L;
	private static int M;
	private static double rc;
	private static double n;
	private static double s;
	private static StringBuffer buffer;
	private static Queue<Double> orderValues;

	public static void run(
			Queue<Particle> particlesFromDynamic,
			double boxSide,
			int matrixSize,
			double interactionRadius,
			int limitTime,
			double noise,
			double speed,
			StringBuffer buff
	) throws CloneNotSupportedException {
		rc = interactionRadius;
		M = matrixSize;
		L = boxSide;
		n = noise;
		s = speed;
		buffer = buff;
		makeMatrix();
		orderValues = new LinkedList<>();

		// Add particles to the cells they belong and print their location
		buffer.append(particlesFromDynamic.size() + "\n");
		buffer.append(0 + "\n");
		for (Particle p : particlesFromDynamic) {

			// Assign cell
			assignCell(p);
			// Print location
			buffer.append(p.getId()+" "+p.getPosition().x + " " + p.getPosition().y + " " + p.getAngle()+"\n");
		}


		// Do the off-lattice magic
		for (int time = 0; time < limitTime; time++) {
			// estoy en Tn y voy a calcular Tn+1
			List<List<CellParticle>> matrixA = cloneMatrix(cells);
			List<Particle> particles = new LinkedList<>();

			// calculamos vecinos
			for (List<CellParticle> cellParticles : cells) {
				for (CellParticle cp : cellParticles) {
					double cellX = cp.cellPosition.x;
					double cellY = cp.cellPosition.y;

					// Check neighbouring cells from inverted up-side down L shape
					visitNeighbour(cp.particle, cellX, cellY, matrixA, cells);
					visitNeighbour(cp.particle, cellX, cellY + 1, matrixA, cells);
					visitNeighbour(cp.particle, cellX + 1, cellY + 1, matrixA, cells);
					visitNeighbour(cp.particle, cellX + 1, cellY, matrixA, cells);
					visitNeighbour(cp.particle, cellX + 1, cellY - 1, matrixA, cells);
				}
			}

			//cambia positions
			for (List<CellParticle> cpList : cells) {
				for (CellParticle cp : cpList) {
					Particle particle = cp.particle;
					calculatePosition(particle);
					calculateAngle(particle);
					// For printing
					particles.add(particle);
				}
			}

			// imprime y blanquea para el siguiente clonado
			makeMatrix();
			buffer.append(particles.size() + "\n");
			buffer.append(time + 1 + "\n");
			for (Particle particle : particles) {
				buffer.append(particle.getId()+" "+particle.getPosition().x + " " + particle.getPosition().y + " " + particle.getAngle()+"\n");
				particle.clearNeighbours();
				assignCell(particle);
			}

			orderValues.add(getOrderValue(particles));
		}
	}

	public static Queue<Double> getOrderValues() {
		return orderValues;
	}

	private static List<List<CellParticle>> cloneMatrix(List<List<CellParticle>> cells) throws CloneNotSupportedException {
		List<List<CellParticle>> clone = new ArrayList<>();

		for (List<CellParticle> cellParticles : cells) {
			List<CellParticle> cloneCellParticles = new ArrayList<>();
			for (CellParticle cellParticle : cellParticles) {
				Particle particleClone = cellParticle.particle.getClone();
				cloneCellParticles.add(new CellParticle(particleClone,
						new Point2D.Double(cellParticle.cellPosition.x, cellParticle.cellPosition.y)));
			}
			clone.add(cloneCellParticles);
		}
		return clone;
	}

	private static void assignCell(Particle p) {
		// Calculate particle's cell indexes
		double cellX = Math.floor(p.getPosition().x / (L / M));
		double cellY = Math.floor(p.getPosition().y / (L / M));

		// Calculate particle's cell number
		int cellNumber = (int) (cellY * M + cellX);

		// Add particle to that cell with cell position
		cells.get(cellNumber).add(new CellParticle(p, new Point2D.Double(cellX, cellY)));
	}

	private static void makeMatrix() {
		cells = new ArrayList<>();
		// Create cells to use
		for (int i = 0; i < M * M; i++)
			cells.add(new ArrayList<>());
	}

	private static void calculatePosition(Particle p) {
		p.getPosition().x += Math.cos(p.getAngle()) * s;
		p.getPosition().y += Math.sin(p.getAngle()) * s;
		if (p.getPosition().x >= L) {
			p.getPosition().x -= L;
		}
		if (p.getPosition().y >= L) {
			p.getPosition().y -= L;
		}
		if (p.getPosition().x < 0) {
			p.getPosition().x += L;
		}
		if (p.getPosition().y < 0) {
			p.getPosition().y += L;
		}
	}

	private static void calculateAngle(Particle particle) {
		Double angle;
		Set<Particle> neighbours = particle.getNeighbours();

		// Average includes particle and its neighbours
		double sin = Math.sin(particle.getAngle());
		double cos = Math.cos(particle.getAngle());

		for (Particle neighbour : neighbours) {
			sin += Math.sin(neighbour.getAngle());
			cos += Math.cos(neighbour.getAngle());
		}

		sin = sin / neighbours.size() + 1;
		cos = cos / neighbours.size() + 1;
		angle = Math.atan2(sin, cos);

		double noise = n * (Math.random() - 1.0 / 2.0);
		angle += noise;

		if (angle > Math.PI) {
			angle -= 2 * Math.PI;
		} else if (angle < -Math.PI) {
			angle += 2 * Math.PI;
		}

		particle.setAngle(angle);
	}

	/**
	 * @param particle es la particula en cuestion en matriz CELL
	 * @param cellX
	 * @param cellY
	 * @param matrixA
	 * @param cells
	 */
	private static void visitNeighbour(Particle particle, double cellX, double cellY, List<List<CellParticle>> matrixA, List<List<CellParticle>> cells) {

		// Reset neighbour cell indexes to comply with contour
		if (cellX >= M) {
			cellX = 0;
		}

		if (cellX < 0) {
			cellX = M - 1;
		}

		if (cellY >= M) {
			cellY = 0;
		}

		if (cellY < 0) {
			cellY = M - 1;
		}


		int neighbourCellNumber = (int) (cellY * M + cellX);

		List<CellParticle> neighbourCellParticles = cells.get(neighbourCellNumber);

		// itero por cada particula vecina en matrixA comparando con la particle que me dieron de matriz CELLS
		for (CellParticle neighbourCellParticle : neighbourCellParticles) {

			Particle neighbourParticle = neighbourCellParticle.particle;

			// chequeo que no sean la misma (una misma particula tiene dos objetos, mirar el id)
			if (neighbourParticle.getId() != particle.getId()) {

				if (particle.getPeriodicDistanceBetween(neighbourParticle, L) < rc) {
					// Mutually add both particles as neighbours
					particle.addNeighbour(neighbourParticle); //a la particula en cells le puse vecina a la de matrizA
					//neighbourParticle.addNeighbour(particle);
					boolean eject = false;
					for (List<CellParticle> cpList : cells) {
						for (CellParticle cp : cpList) {
							if (cp.particle.getId() == neighbourParticle.getId()) {
								neighbourParticle.addNeighbour(particle);
								eject = true;
								break;
							}
						}
						if (eject) break;
					}
				}
			}
//			else{
//			particle.addNeighbour(particle);
//			}
		}
	}

	private static double getOrderValue(List<Particle> particles) {
		double totalVx = 0.0;
		double totalVy = 0.0;

		for (Particle p : particles) {
			totalVx += s * Math.cos(p.getAngle());
			totalVy += s * Math.sin(p.getAngle());
		}

		double totalVi = Math.sqrt(Math.pow(totalVx, 2) + Math.pow(totalVy, 2));

		return totalVi / (particles.size() * s);
	}

	private static class CellParticle {
		Particle particle;
		Point2D.Double cellPosition;

		CellParticle(Particle particle, Point2D.Double cellPosition) {
			this.particle = particle;
			this.cellPosition = cellPosition;
		}
	}

}
