package com.ru.arsenal.check;

public class XOROperator {

  private int a;
  private int b;


  public XOROperator(int a, int b) {
    this.a = a;
    this.b = b;
  }

  public void swapAB(){
    this.a = this.a ^ this.b;
    this.b = this.a ^ this.b;
    this.a = this.a ^ this.b;
  }


  @Override
  public String toString() {
    return "XOROperator{" +
        "a='" + a + '\'' +
        ", b='" + b + '\'' +
        '}';
  }

  public static void main(String[] args) {
    XOROperator operator = new XOROperator(15, 26);
    System.out.println(operator.toString());
    operator.swapAB();
    System.out.println(operator.toString());
  }
}
