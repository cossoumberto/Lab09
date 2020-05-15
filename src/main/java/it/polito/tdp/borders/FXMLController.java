
package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;

import org.jgrapht.Graphs;
import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader
    
    @FXML
    private ComboBox<Country> boxStato;

    @FXML
    private Button btnVicini;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    boolean doCalcolaConfini(ActionEvent event) {
    	Integer anno = -1;
    	try {
    		anno = Integer.parseInt(txtAnno.getText());
    		if(anno<1816 || anno>2016)
    			throw new NumberFormatException();
    	} catch (NumberFormatException e){
    		txtResult.setText("Devi inserire un anno in numeri compreso tra 1816-2016");
    		e.printStackTrace();
    	}
    	if(anno>=1816 && anno<=2016) {
	    	model.creaGrafo(anno);
	    	txtResult.setText("Stati connessi nell'anno " + anno);
	    	for(Country c : model.getGrafo().vertexSet()) {
	    		txtResult.appendText("\n" + c.toString() + ": " + Graphs.neighborListOf(model.getGrafo(), c).size());
	    	}
	    	txtResult.appendText("\nIl numero di componenti connesse è " + model.getComponentiConnesse());
	    	txtResult.appendText("\ndi cui " + model.getComponentiConnesseNonSole() + " sono composte da almeno 2 stati");
	    	return true;
    	}
    	return false;
    }
    
    @FXML
    void doTrovaVicini(ActionEvent event) {
    	if(this.doCalcolaConfini(event)) {
	    	if(boxStato.getValue()!=null && model.getGrafo().containsVertex(boxStato.getValue())) {
	    		txtResult.setText("Stati raggiungibili via terra dallo stato " + boxStato.getValue() + ": ");
	    		//for(Country c : model.visitaAmpiezza(boxStato.getValue()))
	    		for(Country c : model.visitaConRicorsiva(boxStato.getValue()))
	    			txtResult.appendText("\n" + c.toString());
	    	} else if(boxStato.getValue()==null) {
	    		txtResult.setText("Devi selezionare uno stato");
	    	}else
	    		txtResult.setText("Stato non esistente o privo di connessioni nell'anno scelto");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnVicini != null : "fx:id=\"btnVicini\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxStato.getItems().addAll(model.loadAllCountries());
    }
}
