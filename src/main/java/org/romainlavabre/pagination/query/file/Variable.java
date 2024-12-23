package org.romainlavabre.pagination.query.file;

/**
 * @author Romain Lavabre <romain.lavabre@proton.me>
 */
public class Variable {

    private final String name;

    private final String value;


    public Variable( String name, String value ) {
        this.name  = name;
        this.value = value;
    }


    public String getName() {
        return name;
    }


    public String getValue() {
        return value;
    }
}
