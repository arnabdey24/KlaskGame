public class ServeArea {

    private double start;
    private double extent;
    private double x;
    private double y;
    private double w;
    private double h;
    private String colour;
    private double thickness;

    public ServeArea(double x, double y, double w, double h, double start, double extent, String colour, double thickness) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.start = start;
        this.extent = extent;
        this.colour = colour;
        this.thickness = thickness;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public double getStart() {
        return start;
    }

    public double getExtent() {
        return extent;
    }

    public String getColour() {
        return colour;
    }

    public double getThickness() {
        return thickness;
    }
}
