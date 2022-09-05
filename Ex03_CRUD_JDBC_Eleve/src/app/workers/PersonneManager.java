/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.workers;

import app.beans.Personne;
import java.util.List;

/**
 *
 * @author MartinK
 */
public class PersonneManager {
    private List<Personne> listePersonnes;
    private int index = 0;
    
    public Personne setPersonnes(List<Personne> listePersonnes){
        this.listePersonnes = listePersonnes;
        return this.listePersonnes.get(0);
    }
    public Personne precedentPersonne(){
        if(index !=0){
            index--;
        }
        return listePersonnes.get(index);
    }
    public Personne suivantPersonne(){
        if(index != listePersonnes.size()-1){
            index++;
        }
        return listePersonnes.get(index);
    }
    public Personne debutPersonne(){
        return listePersonnes.get(0);
    }
    public Personne finPersonne(){
        index = listePersonnes.size()-1;
        return listePersonnes.get(index);
    }
    public Personne courantPersonne(){
        if(listePersonnes == null || listePersonnes.isEmpty()){
            return null;
        }
        if(index >= listePersonnes.size()){
            index = listePersonnes.size() -1;
        }
        return listePersonnes.get(index);
    }
}
