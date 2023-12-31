package view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import control.ChangeSceneController;
import control.ProductController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Product;
import utils.SceneName;
import utils.UserSession;

/**
 * Construtor da tela de pagina inicial, com a implementação da interface GenericWindownInterface.
 */
public class WinHomePageConstructor implements GenericWindownInterface
{
	private Pane pWin;

	private List<Product> listProduct;
	
	private ChangeSceneController changeSceneController = new ChangeSceneController();
	/**
     * Adiciona elementos à interface gráfica da janela de conta.
     *
     * @param pane O painel ao qual os elementos serão adicionados.
     */
	public void addElements(Pane pane)
	{
		this.pWin = pane;
		
		Label lblHomePage = new Label("Página Inicial");
		lblHomePage.setPrefHeight(35);
		lblHomePage.setPrefWidth(640);
		lblHomePage.setStyle("-fx-font-size: 24px; -fx-alignment: center; -fx-font-weight: bold");
		lblHomePage.setLayoutY(25);
		
		
		Button btnQuit = new Button("Sair❌");
		Button btnAccount = new Button("Conta");
		Button btnCartStore;
		setOverButtonStyle(btnQuit);
		setOverButtonStyle(btnAccount);
		if(UserSession.getUserType().contains("client"))
        {
        	btnCartStore = new Button("Carrinho🛒");
        }
        else
        {
        	btnCartStore = new Button("Loja🏬");        	
        }
		setOverButtonStyle(btnCartStore);
		
		
		HBox hbOption = new HBox();
		hbOption.setPrefWidth(640);
		hbOption.setPrefHeight(30);
		hbOption.setPadding(new Insets(0, 15, 0, 0));
		hbOption.setStyle("-fx-alignment: top-right; -fx-spacing: 15px;");
		hbOption.getChildren().addAll(btnCartStore, btnQuit, btnAccount);
		
		TextField tfSearch = new TextField();
		tfSearch.setPromptText("Buscar item");
		Button btnSearch = new Button("🔍");
		tfSearch.setPrefWidth(560);
		
		HBox hbSearch = new HBox();
		hbSearch.setPrefWidth(640);
		hbSearch.setPrefHeight(30);
		hbSearch.setLayoutY(60);
		hbSearch.setPadding(new Insets(0, 0, 0, 20));
		hbSearch.setStyle("-fx-spacing: 13px");
		hbSearch.getChildren().addAll(tfSearch, btnSearch);
		
		//Montagem da lista de acordo com o banco de dados para poder puxar todos os produtos direto para a pane
		ProductController pControl = new ProductController();
		
		try {
			listProduct = pControl.listProduct();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		VBox vbProduct = new VBox();
		vbProduct.setMinHeight(276);
		vbProduct.setMinWidth(586);
		vbProduct.setMaxWidth(586);
		
		ScrollPane spProduct = new ScrollPane();
		spProduct.setPrefHeight(282);
		spProduct.setPrefWidth(605);
		spProduct.setLayoutX(18);
		spProduct.setLayoutY(91);
		spProduct.setStyle("-fx-border-color: black; -fx-border-radius: 10px; -fx-border-width: 2px;");
		spProduct.setContent(vbProduct);		
		
		completeListProduct(vbProduct);
		
		btnSearch.setOnAction(e -> 
		{
			if(tfSearch.getText().trim().isEmpty())
			{
				completeListProduct(vbProduct);
			}
			else
			{
				searchListProduct(vbProduct, tfSearch);
			}
		});
		
		tfSearch.setOnKeyPressed(e ->
		{
			if(e.getCode() == KeyCode.ENTER)
			{
				btnSearch.fire();
			}
		});
		
		
		//------------mudança de scene---------------
		btnQuit.setOnAction(e -> toLogin());
		btnCartStore.setOnAction(e -> toCartStore(btnCartStore));
		btnAccount.setOnAction(e -> toAccount());
		
		pane.getChildren().addAll(hbOption, lblHomePage, hbSearch, spProduct);
		
		
	}

	private void toLogin() 
	{
		changeSceneController.changeScene(SceneName.LOGIN, this.pWin);
	}
	
	private void toAccount() 
	{
		changeSceneController.changeScene(SceneName.ACCOUNT_MENU, this.pWin);
	}
	
	private void toProduct(IntegerProperty cod)
	{
		changeSceneController.setCodValue(cod);
		changeSceneController.changeScene(SceneName.CONSULT_PRODUCT, this.pWin);
	}
	
	private void toCartStore(Button btnCartStore)
	{
		if(btnCartStore.getText().equals("Carrinho🛒"))
		{
			changeSceneController.changeScene(SceneName.CART, this.pWin);
		}
		else
		{
			changeSceneController.changeScene(SceneName.STORE, this.pWin);
		}
	}
	
	private void completeListProduct(VBox vbProduct)
	{
		vbProduct.getChildren().clear();
		
		tableProduct(listProduct, vbProduct);
	}
	
	private void searchListProduct(VBox vbProduct, TextField tfSearch)
	{
		vbProduct.getChildren().clear();
		
		List<Product> searchListProduct = new ArrayList<>();
		
		for(Product pList : listProduct)
		{
			if(pList.getName().toLowerCase().contains(tfSearch.getText().toLowerCase()))
			{
				searchListProduct.add(pList);
			}
		}
		
		tableProduct(searchListProduct, vbProduct);
	}
	
	private void tableProduct(List<Product> list, VBox vbProduct)
	{
		int sizeProductRow = list.size();
		int sizeProductColumn = sizeProductRow;
		int countProduct = 0;
		
		for(int i = 0; i < (sizeProductRow / 4) + 1; i++) //Aqui é feito a contagem da linha para descer de acordo com o tanto de produto no DB
		{
			HBox hbProductInfo = new HBox();
			hbProductInfo.setPrefWidth(586);
			hbProductInfo.setMaxWidth(586);
			hbProductInfo.setPrefHeight(100);
			for(int j = 0; j < 4; j++) //Aqui é feito a contagem de coluna para ser de acordo com a quantidade de produto
			{
				if(sizeProductColumn > 0)
				{
					Product p = list.get(countProduct);
					Label lblNameProduct = new Label(p.getName());
					Label lblDescProduct = new Label(p.getDescription());
					Label lblPrice = new Label("Preço: R$" + String.valueOf(p.getPrice()).replace(".", ","));
					lblDescProduct.setPrefHeight(55);
					lblDescProduct.setWrapText(true);
					lblDescProduct.setStyle("-fx-alignment: top-left");
					
					HBox hbLblNameProduct = new HBox();
					hbLblNameProduct.setMinHeight(20);
					hbLblNameProduct.setStyle("-fx-alignment: center; -fx-font-weight: bold; -fx-font-size: 14px;");
					hbLblNameProduct.getChildren().addAll(lblNameProduct);
					
					HBox hbLblDescProduct = new HBox();
					hbLblDescProduct.setMinHeight(50);
					hbLblDescProduct.getChildren().add(lblDescProduct);
					
					HBox hbLblPrice = new HBox();
					hbLblPrice.getChildren().add(lblPrice);
					
					VBox vbProductInfo = new VBox();
					vbProductInfo.setPrefWidth(146);
					vbProductInfo.setPrefHeight(100);
					vbProductInfo.setPadding(new Insets(0, 0, 0, 5));
					vbProductInfo.setStyle("-fx-border-color: black; -fx-border-radius: 10px; -fx-border-width: 2px; -fx-cursor: hand;");
					vbProductInfo.getChildren().addAll(hbLblNameProduct, hbLblDescProduct, hbLblPrice);
					
					IntegerProperty codProperty = new SimpleIntegerProperty(p.getCod());
					
					vbProductInfo.setOnMouseClicked(e -> toProduct(codProperty));
					
					hbProductInfo.getChildren().add(vbProductInfo);
					
					sizeProductColumn--;
					countProduct++;
				}
				else
				{
					break;
				}
				
			}
			
			vbProduct.getChildren().add(hbProductInfo);
		}
	}

    private void setBtnStyle(Button button, String style) {
        button.setStyle(style);
    }


    private void setOverButtonStyle(Button button) 
    {
    	String styleEnter = "-fx-border-color: rgba(255,255,255,0); -fx-cursor: hand; " +
                "-fx-background-color: rgba(94,94,94,0.26); -fx-background-radius: 1000px";
    	String styleExit = "-fx-border-color: rgba(255,255,255,0); -fx-cursor: hand; " +
                "-fx-background-color: rgba(255,255,255,0);";
        button.setOnMouseEntered(e -> setBtnStyle(button, styleEnter));
        button.setOnMouseExited(e -> setBtnStyle(button, styleExit));
        button.setStyle(styleExit);
    }

}
