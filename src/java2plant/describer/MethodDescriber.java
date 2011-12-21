/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package java2plant.describer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arthur
 */


public class MethodDescriber {

    private Visibility visibility;
    private String returnType;
    private String name;
    private boolean isAbstract;
    private ArrayList<ArgumentDescriber> args = new ArrayList();

    MethodDescriber(String str) {
        this.visibility = new Visibility("private");
        this.isAbstract = false;
        this.buildFromString(str);
    }

    private void buildFromString(String str) {
        String[] split = str.split(" ");
        int i=0;
        while(i < split.length ) {
            if(split[i].isEmpty()) {
                i++;
            } else if(split[i].equals("public") || split[i].equals("private") ||
                    split[i].equals("protected") || split[i].equals("package")) {
                this.setVisibility(split[i]);
                i++;
            } else if(split[i].equals("static")) {
                i++;
            } else if(split[i].contains("final")) {
                i++;
            } else if(split[i].contains("abstract")) {
                this.isAbstract = true;
                i++;
            } else if(split[i].startsWith("@")) {
                i++;
            } else {
                if(split[i].contains("(")) {
                    this.setReturnType("");
                } else {
                    this.setReturnType( split[i]);
                    i++;
                }
                this.setName(split[i].substring(0, split[i].indexOf('(')));
                i = split.length; //exit
            }

        }
        /* Construction des arguments */
        int a = str.indexOf("(");
        int b = str.indexOf(")");

        str = str.substring(str.indexOf("(")+1, str.indexOf(")"));
        if(!str.isEmpty()) {
            split = str.split(",");
            for(int j=0; j<split.length; j++) {
                ArgumentDescriber arg = new ArgumentDescriber(split[j]);
                args.add(arg);
            }
            
        }
    }
    
    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(String vis) {
        this.visibility = new Visibility(vis);
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public void print() {
        System.out.println("Method : " + visibility +" "+returnType+" "+name);
        System.out.print("-- Args : ");
        for(Iterator it = args.iterator(); it.hasNext() ;) {
            ArgumentDescriber arg = (ArgumentDescriber) it.next();
            System.out.print(arg);
        }
        System.out.println("");
    }

    void writeUML(BufferedWriter bw) {
        try {
            this.visibility.writeUML(bw);
            bw.write(this.name+"(");
            for(Iterator it = args.iterator(); it.hasNext() ;) {
                ArgumentDescriber arg = (ArgumentDescriber) it.next();
                arg.writeUML(bw);
                if(it.hasNext()) {
                    bw.write(", ");
                }
            }
            if(this.returnType.equals("void")) {
                bw.write(")");
            } else {
                bw.write("):"+this.returnType);
            }
            bw.newLine();

        } catch (IOException ex) {
            Logger.getLogger(MethodDescriber.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
