package seungju.fishcaketycoon;

/**
 * Created by LKH on 2015-12-01.
 */
class Product {

    private Item item;

    private int count;
    private int totalPrice;
    private int imageID;

    public Product(Item psz_item, int psz_count) {
        setItem(psz_item);
        setCount(psz_count);
        imageID = 0;
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getImageID() {
        return imageID;
    }

    public void setItem(Item psz_item) {
        item = psz_item;
    }

    public void setCount(int psz_count) {
        count = psz_count;
        if (item != null) totalPrice = count * item.getPrice();
        else totalPrice = 0;
    }

    public void setImageID(int id) {
        imageID = id;
    }
}