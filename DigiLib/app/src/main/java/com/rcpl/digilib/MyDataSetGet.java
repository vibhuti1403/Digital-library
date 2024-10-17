package com.rcpl.digilib;

/**
 * Created by HP on 09-Jan-18.
 */

public class MyDataSetGet {
    String NAME;
    String AUTHOR;
    String URL;
    String BOOKID;
    String VISIBILITY;

    public void setBOOKID(String BOOKID) {
        this.BOOKID = BOOKID;
    }

    public void setVISIBILITY(String VISIBILITY) {
        this.VISIBILITY = VISIBILITY;
    }

    public String getVISIBILITY() {
        return VISIBILITY;
    }

    public String getBOOKID() {
        return BOOKID;
    }


    public String getIMAGES() {
        return IMAGES;
    }

    public void setIMAGES(String IMAGES) {
        this.IMAGES = IMAGES;
    }
    String IMAGES;
    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }



    public String getAUTHOR() {
        return AUTHOR;
    }

    public void setAUTHOR(String AUTHOR) {
        this.AUTHOR = AUTHOR;
    }




}
