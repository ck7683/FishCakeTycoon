package seungju.fishcaketycoon;

/**
 * Created by LKH on 2015-12-01.
 */
class ShowCase {

    private Product[] table;

    public ShowCase() {
        table = new Product[8];
        for (Product e : table) {
            e = null;
        }
    }

    public Product getProduct(int tableNumber) {
        if (tableNumber < 1 || tableNumber > 8) return null;
        else {
            return table[tableNumber - 1];
        }
    }

    public Product getProduct(int rowTable, int colTable) {
        if (colTable < 1 || colTable > 4) return null;
        return getProduct(rowTable * 4 + colTable - 4);
    }

    public int setProduct(int tableNumber, Product psz_product) {
        if (tableNumber < 1 || tableNumber > 8) return -1;
        else {
            table[tableNumber - 1] = psz_product;
            return 0;
        }
    }

    public int setProduct(int rowTable, int colTable, Product psz_product) {
        if (colTable < 1 || colTable > 4) return -1;
        return setProduct(rowTable * 4 + colTable - 4, psz_product);
    }
}
