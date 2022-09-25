public class Planet {
  public double xxPos;
  public double yyPos;
  public double xxVel;
  public double yyVel;
  public double mass;
  public String imgFileName;
  private static final double G = 6.67e-11;

  public Planet(double xxPos, double yyPos, double xxVel, double yyVel, double mass, String imgFileName) {
    this.xxPos = xxPos;
    this.yyPos = yyPos;
    this.xxVel = xxVel;
    this.yyVel = yyVel;
    this.mass = mass;
    this.imgFileName = imgFileName;
  }

  public Planet(Planet p) {
    // this.xxPos = p.xxPos;
    // this.yyPos = p.yyPos;
    // this.xxVel = p.xxVel;
    // this.yyVel = p.yyVel;
    // this.mass = p.mass;
    // this.imgFileName = p.imgFileName;
    this(p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
  }

  /** Calculates the distance between this Planet and Planet p */
  public double calcDistance(Planet p) {
    double dx = xxPos - p.xxPos;
    double dy = yyPos - p.yyPos;
    return Math.sqrt(dx * dx + dy * dy);
  }

  /** Calculates force exerted on this Planet by Planet p */
  public double calcForceExertedBy(Planet p) {
    double distance = calcDistance(p);
    return (G * mass * p.mass) / (distance * distance);
  }

  /** Calculates force exerted on this Planet by Planet p on the x-axis */
  public double calcForceExertedByX(Planet p) {
    return calcForceExertedBy(p) * (p.xxPos - xxPos) / calcDistance(p);
  }

  /** Calculates force exerted on this Planet by Planet p on the y-axis */
  public double calcForceExertedByY(Planet p) {
    return calcForceExertedBy(p) * (p.yyPos - yyPos) / calcDistance(p);
  }

  /** Calculates net force exerted on this Planet by Planet p on the x-axis */
  public double calcNetForceExertedByX(Planet[] planets) {
    double netForceX = 0;
    for (Planet p : planets) {
      if (this.equals(p)) {
        continue;
      }
      netForceX += calcForceExertedByX(p);
    }
    return netForceX;
  }

  /** Calculates net force exerted on this Planet by Planet p on the x-axis */
  public double calcNetForceExertedByY(Planet[] planets) {
    double netForceY = 0;
    for (Planet p : planets) {
      if (this.equals(p)) {
        continue;
      }
      netForceY += calcForceExertedByY(p);
    }
    return netForceY;
  }

  public void update(double dt, double fX, double fY) {
    // acceleration along x-axis and y-axis
    double aX = fX / mass;
    double aY = fY / mass;

    // new velocity
    xxVel = xxVel + aX * dt;
    yyVel = yyVel + aY * dt;

    // new position (this is a rough formula)
    xxPos = xxPos + xxVel * dt;
    yyPos = yyPos + yyVel * dt;
  }

  public void draw() {
    StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
  }
}