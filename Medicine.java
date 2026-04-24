
public class Medicine {
    public String name, time, relation;
    public int stock;

    public Medicine(String name, String time, String relation, int stock) {
        this.name = name;
        this.time = time;
        this.relation = relation;
        this.stock = stock;
    }

    public void displayMed() {
        System.out.println("\t\t\t-----------------------------------------");
        System.out.println("\t\t\t  Medicine : " + name);
        System.out.println("\t\t\t  Time     : " + time);
        System.out.println("\t\t\t  Relation : " + relation);
        System.out.println("\t\t\t  Stock    : " + stock);
        
        if (stock <= 5) {
            System.out.println("\n\t\t\t  [ ALERT: LOW STOCK ]");
            System.out.println("\t\t\t  Please refill " + name + " soon!");
        }
        System.out.println("\t\t\t-----------------------------------------");
    }
}