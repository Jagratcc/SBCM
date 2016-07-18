package fiveexceptions.com.m01sbcm.model;

/**
 * Created by amit on 16/4/16.
 */
public class Education {

    int id;
    String name;


    public Education(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
