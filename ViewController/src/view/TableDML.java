package view;

import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;

import oracle.binding.OperationBinding;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;


public class TableDML {
    public TableDML() {
    }

   
    
    public void deleteSelectedRows(ActionEvent actionEvent) {
        List<Key> keyList = new ArrayList<Key>();
        System.out.println("keyList 1: " + keyList);
        DCBindingContainer bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter = bindings.findIteratorBinding("EngStudentVOView1Iterator");
        
       for (int i = 0; i < iter.getViewObject().getEstimatedRowCount(); i++) 
       {
           Row row = iter.getRowAtRangeIndex(i);
           
           System.out.println("iter.getRowAtRangeIndex(i) : " + iter.getRowAtRangeIndex(i));
           if (row.getAttribute("SelectRow") != null)
           {
             String selectedResult = row.getAttribute("SelectRow").toString();
             if (selectedResult.equalsIgnoreCase("Y"))
                 keyList.add(row.getKey());
           }
           System.out.println("Selected keyList 2: " + keyList);
      }
               
                
       DCIteratorBinding dci = bindings.findIteratorBinding("EngStudentVOView1Iterator");
       ViewObject vo = dci.getViewObject();
       for (int i = 0; i < keyList.size(); i++) {
           Row row = vo.getRow(keyList.get(i));
           vo.setCurrentRow(row);
           vo.removeCurrentRow();
       }
       OperationBinding operation = (OperationBinding) BindingContext.getCurrent()
                                                                     .getCurrentBindingsEntry()
                                                                     .get("Commit");
       operation.execute();      
       keyList.clear();

    }

    public void checkValListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...

        try {
            
           System.out.println("In Header selectAllChoiceBox value = " + valueChangeEvent.getNewValue());
            boolean isSelected = ((Boolean) valueChangeEvent.getNewValue()).booleanValue();

            DCBindingContainer bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding iter = bindings.findIteratorBinding("EngStudentVOView1Iterator");
            ViewObject vo = iter.getViewObject();
            vo.reset();
            int i = 0;
            Row row = null;
            while (vo.hasNext()) {
                if (i == 0)
                    row = vo.first();
                else
                    row = vo.next();

                if (isSelected)
                    row.setAttribute("SelectRow", "Y");
                else
                    row.setAttribute("SelectRow", "N");

                i++;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            System.out.println("checkValListener:Exception::"+e.toString());
        }
        
    }
}
