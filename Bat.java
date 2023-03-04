import java.util.HashSet;

public class Bat extends Ball{

    private HashSet<Integer> magnets;


    public Bat(double x, double y, double diameter, String col) {
        super(x, y, diameter, col);
        magnets=new HashSet<>();
    }

    public Bat(double x, double y, double diameter, String col, int layer) {
        super(x, y, diameter, col, layer);
        magnets=new HashSet<>();
    }

    public int getMagnets() {
        return magnets.size();
    }

    public void addMagnet(int id){
        magnets.add(id);
    }

    public void clear(){
        magnets.clear();
    }

    public boolean collidesWithMagnet(Ball b) {
        double dx = b.getXPosition() - getXPosition();
        double dy = b.getYPosition() - getYPosition();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < 30;
    }
}
