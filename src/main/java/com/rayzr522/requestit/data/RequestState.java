/**
 * 
 */
package com.rayzr522.requestit.data;

import java.util.Arrays;

import org.bukkit.DyeColor;

/**
 * @author Rayzr
 *
 */
public enum RequestState {

    UNVIEWED(0, DyeColor.RED),
    VIEWED(1, DyeColor.YELLOW),
    COMPLETED(2, DyeColor.GREEN);

    private int statusCode;
    private DyeColor color;

    RequestState(int statusCode, DyeColor color) {
        this.statusCode = statusCode;
        this.color = color;
    }

    /**
     * @return The status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @return The color
     */
    public DyeColor getColor() {
        return color;
    }

    public static RequestState getState(int status) {
        return Arrays.stream(values()).filter(s -> s.statusCode == status).findFirst().orElse(null);
    }

}
