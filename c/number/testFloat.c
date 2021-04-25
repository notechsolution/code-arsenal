#include <stdio.h>

void main(void)
{
    int num = 9;
    float *pFloat = &num;
    printf("num value is : %d\n", num);
    printf("float value is %f\n", *pFloat);
    *pFloat = 9.0;
    printf("num value is :%d\n", num);
    printf("*pFloat value is %f\n", *pFloat);
}