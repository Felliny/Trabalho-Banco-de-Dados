package control;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.TextFieldTableCell;
import model.*;
import persistence.GenericDao;
import persistence.OrderHistoryDao;
import persistence.PurchaseHistoryDao;
import utils.UserSession;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Esta Classe de Controller que realiza as operações das Classes WinPurchaseHistoryConstructor e WinOrderHistoryConstructor.
 */
public class OrderHistoryController extends TextFieldTableCell<Order, List<Item>> {

    private ObservableList<Order> listHistory= FXCollections.observableArrayList();
    private StringProperty nameProduct= new SimpleStringProperty("");
    private StringProperty nameStore= new SimpleStringProperty("");
    private IntegerProperty quantity= new SimpleIntegerProperty(0);
    private StringProperty priceProduct= new SimpleStringProperty("00.00");
    private StringProperty methodPayment= new SimpleStringProperty("");
    private StringProperty totalValue= new SimpleStringProperty("00.00");
    private StringProperty portage = new SimpleStringProperty("00.00");
    private StringProperty status= new SimpleStringProperty("");

    /**
     * Carrega os dados para tela de histórico de compras.
     * @return A lista de compras.
     * @throws SQLException traz exceções de SQL.
     */
    public ObservableList populateWinHistory() throws SQLException {
        Client client= new Client();
        client.setLogin(UserSession.getUserName());
        GenericDao genericDao= new GenericDao();
        PurchaseHistoryDao purchaseHistoryDao= new PurchaseHistoryDao(genericDao);

        List<Order> listOrder= purchaseHistoryDao.listPurchaseHistory(client);

        if (!listOrder.isEmpty()){
            for (int i = 0; i < listOrder.size(); i++) {
                Order order= (Order) listOrder.get(i);

                listHistory.add(order);
            }
        }
        return listHistory;
    }

    /**
     * Carrega os dados para tela de status da compra.
     * @param order A compra.
     * @throws SQLException traz exceções de SQL.
     */
    public void populateWinStatus(Order order) throws SQLException {
        List<Item> itemList= order.getItems();
        Item item= itemList.get(0);

        Integer idOrder= order.getId();
        Integer idProduct= item.getProduct().getCod();
        GenericDao genericDao= new GenericDao();
        PurchaseHistoryDao purchaseHistoryDao= new PurchaseHistoryDao(genericDao);
        order= purchaseHistoryDao.returnStatusProduct(idOrder, idProduct);
        Store store= purchaseHistoryDao.returnNameStore(idOrder, idProduct);

        itemList= order.getItems();
        item= itemList.get(0);

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        nameProduct.set(item.getProduct().getName());
        nameStore.set(store.getNameStore());
        quantity.set(item.getQuantity());
        String formatedValue= decimalFormat.format(item.getProduct().getPrice());
        priceProduct.set("R$ " +formatedValue);
        methodPayment.set(order.getPayment().getPaymentMethod());
        formatedValue= decimalFormat.format(item.getSubTotal());
        totalValue.set("R$ " +formatedValue);
        status.set(order.getPayment().getStatus());
        formatedValue= decimalFormat.format(item.getProduct().getShipping());
        portage.set("R$ " + formatedValue);
    }

    /**
     * Carrega dados para a tela historico de pedidos.
     * @return A lista de pedidos
     * @throws SQLException traz exceções de SQL.
     */
    public ObservableList<Order> populateWinOrderHistory() throws SQLException {
        Store store= new Store();
        store.setNameStore(UserSession.getUserName());
        GenericDao genericDao= new GenericDao();
        OrderHistoryDao orderHistoryDao= new OrderHistoryDao(genericDao);

        List<Order> listOrder= orderHistoryDao.listOrderHistory(store);

        if (listOrder != null){
            for (int i = 0; i < listOrder.size(); i++) {
                Order order= (Order) listOrder.get(i);

                listHistory.add(order);
            }
        }
        return listHistory;

    }


    /**
     * Obtêm a lista de Order
     * @return A lista de Order
     */
    public ObservableList<Order> getListHistory() {
        return listHistory;
    }

    /**
     * Obtêm o campo do nome do produto.
     * @return o campo nome do produto.
     */
    public StringProperty nameProductProperty() {
        return nameProduct;
    }

    /**
     * Obtêm o campo do nome da loja.
     * @return o campo nome da loja.
     */
    public StringProperty nameStoreProperty() {
        return nameStore;
    }

    /**
     * Obtêm o campo da quantidade.
     * @return o campo quantidade.
     */
    public IntegerProperty quantityProperty() {
        return quantity;
    }

    /**
     * Obtêm o campo de preço do produto.
     * @return o campo preço do produto
     */
    public StringProperty priceProductProperty() {
        return priceProduct;
    }

    /**
     * Obtêm o campo de metodo de pagamento
     * @return o campo metodo pagamento
     */
    public StringProperty methodPaymentProperty() {
        return methodPayment;
    }

    /**
     * Obtêm o campo de valor total
     * @return o campo valor total
     */
    public StringProperty totalValueProperty() {
        return totalValue;
    }

    /**
     * obtêm o campo de frete
     * @return o campo frete
     */
    public StringProperty portageProperty() {
        return portage;
    }

    /**
     * obtêm o campo de status
     * @return o campo status
     */
    public StringProperty statusProperty() {
        return status;
    }
}
