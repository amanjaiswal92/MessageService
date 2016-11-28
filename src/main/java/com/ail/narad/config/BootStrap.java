package com.ail.narad.config;

/**
 * Created by tech on 12/10/16.
 */
@FunctionalInterface
public interface BootStrap {
    public void setup() throws Exception;
}


