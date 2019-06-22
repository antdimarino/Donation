package com.example.donation;


public class ItemProject
{
    private String descrizione;
    private String email;
    private String onlus;
    private String titolo;

    public ItemProject()
    {

    }

    public ItemProject(String titolo, String descrizione, String onlus, String email)
    {
        this.descrizione = descrizione;
        this.email = email;
        this.onlus = onlus;
        this.titolo = titolo;
    }


    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOnlus() {
        return onlus;
    }

    public void setOnlus(String onlus) {
        this.onlus = onlus;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
}
