/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pizzaria.util;

public class GerarHash {

    public static void main(String[] args) {

        String senhaAtendente = "456";
        String senhaMotoboy   = "789";

        System.out.println("Hash atendente (456):");
        System.out.println(Criptografia.hashSenha(senhaAtendente));

        System.out.println();

        System.out.println("Hash motoboy (789):");
        System.out.println(Criptografia.hashSenha(senhaMotoboy));
    }
}


