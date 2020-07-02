package seungju.fishcaketycoon;

/**
 * Created by LKH on 2015-12-01.
 */
class Item {

    private String name;
    private int price;

    private void create(String psz_name, int psz_price) {
        setName(psz_name);
        setPrice(psz_price);
    }

    public Item(String psz_name, int psz_price) {
        create(psz_name, psz_price);
    }

    public Item(Item psz_item) {
        create(psz_item.name, psz_item.price);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void setName(String psz_name) {
        name = new String(psz_name);
    }

    public void setPrice(int psz_price) {
        price = psz_price;
    }
}
