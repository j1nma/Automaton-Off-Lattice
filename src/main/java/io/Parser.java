package io;

import models.Particle;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Parser for dynamic file.
 */
public class Parser {

	private String dynamicFilePath;

	private int numberOfParticles;
	private Queue<Particle> particles;

	public Parser(String dynamicFilePath) {
		this.dynamicFilePath = dynamicFilePath;
		this.particles = new LinkedList<>();
	}

	public boolean parse() {
		return parseDynamicFile();
	}

	private boolean parseDynamicFile() {
		File dynamicFile = new File(dynamicFilePath);
		Scanner sc;
		try {
			sc = new Scanner(dynamicFile);
		} catch (FileNotFoundException e) {
			System.out.println("Dynamic file not found exception: " + dynamicFilePath);
			return false;
		}
		sc.nextInt();   // t0 not used
		for (int i = 0; i < numberOfParticles; i++) {
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			Particle particle = particles.poll();
			particle.setPosition(new Point2D.Double(x, y));
			particles.add(particle);
		}
		sc.close();
		return true;
	}

	public String getDynamicFilePath() {
		return dynamicFilePath;
	}

	public void setDynamicFilePath(String dynamicFilePath) {
		this.dynamicFilePath = dynamicFilePath;
	}

	public int getNumberOfParticles() {
		return numberOfParticles;
	}

	public void setNumberOfParticles(int numberOfParticles) {
		this.numberOfParticles = numberOfParticles;
	}

	public Queue<Particle> getParticles() {
		return particles;
	}

	public void setParticles(Queue<Particle> particles) {
		this.particles = particles;
	}
}
