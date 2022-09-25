public class NBody {

  /** Read the radius from the specified fileName */
  public static double readRadius(String fileName) {
    In in = new In(fileName);
    in.readInt();
    double radius = in.readDouble();
    return radius;
  }

  /** Create an array of Planets by data in fileName */
  public static Planet[] readPlanets(String fileName) {
    In in = new In(fileName);

    // read N and radius in the file
    int N = in.readInt();
    in.readDouble();

    Planet[] planets = new Planet[N];

    /* read data of each planet, create a Planet based on 
     * those data and store the Planet p in planets */
    for (int i = 0; i < N; i++) {
      double xxPos = in.readDouble();
      double yyPos = in.readDouble();
      double xxVel = in.readDouble();
      double yyVel = in.readDouble();
      double mass = in.readDouble();
      String imgFileName = in.readString();

      Planet p = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
      planets[i] = p;
    }
    
    return planets;
  }

  public static void main(String[] args) {
    // Read input from command line
    double T = Double.parseDouble(args[0]);
    double dt = Double.parseDouble(args[1]);
    String fileName = args[2];

    // Read input from file fileName 
    double radius = readRadius(fileName);
    Planet[] planets = readPlanets(fileName);

    // set the scale of the universe and DoubleBuffering
    StdDraw.setScale(-radius, radius);
    StdDraw.enableDoubleBuffering();

    // clears the drawing window, and draw the background
    StdDraw.clear();
    StdDraw.picture(0, 0, "images/starfield.jpg", 2 * radius, 2 * radius);

    // Draw Planets
    for (Planet p : planets) {
      p.draw();
    }

    // Display drawing
    StdDraw.show();

    // Create animation of planets
    for (double time = 0; time < T; time += dt) {
      int numPlanets = planets.length;
      
      // Calculate net xy forces for each planet
      double[] xForces = new double[numPlanets];
      double[] yForces = new double[numPlanets];
      for (int i = 0; i < numPlanets; i++) {
        xForces[i] = planets[i].calcNetForceExertedByX(planets);
        yForces[i] = planets[i].calcNetForceExertedByY(planets);
      }

      // update each planet (we seperate this step because autograder asks us to)
      for (int i = 0; i < numPlanets; i++) {
        planets[i].update(dt, xForces[i], yForces[i]);
      }

     
      // clears the drawing window
      StdDraw.clear();

      // Draw background and planets
      StdDraw.picture(0, 0, "images/starfield.jpg", 2 * radius, 2 * radius);
      for (Planet p : planets) {
        p.draw();
      }

      // Display updated drawing
      StdDraw.show();

      // pause for 10 ms
      StdDraw.pause(10);
    }

    // print final state of the universe after end of simulation
    Stdout.printf("%d\n", planets.length);
    Stdout.printf("%.2e\n", radius);
    for (Planet p : planets) {
      Stdout.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, 
                    p.imgFileName);
    }
  }
}