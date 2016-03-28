/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.serpro.jnlp.rest;

/**
 *
 * @author 07721825741
 */
public class MyMessage {

    private String message;

    public MyMessage() {
    }

    public MyMessage(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return new StringBuffer(" message : ").append(this.message).toString();
    }
}
