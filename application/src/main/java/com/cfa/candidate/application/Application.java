package com.cfa.candidate.application;

import io.micronaut.runtime.Micronaut;

public class Application {

  public static void main(String[] args) {
    Micronaut.build(args).packages("com.cfa.candidate").start();
  }
}
