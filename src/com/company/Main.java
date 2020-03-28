package com.company;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) {
        Parser newPars = new Parser();
        Scanner input = new Scanner(System.in);
        int Alloc;
       /* DiskAllocator myAlloc;
        System.out.println("How Would you like to allocate? \n 1.Contiguous Allocation \n 2.Indexed Allocation");
        Alloc = input.nextInt();
       switch (Alloc){
            case 1:
                myAlloc = new ContiguousAllocator(3);
                newPars = new Parser(myAlloc);
                break;
            case 2:
                //myAlloc = new IndexedAllocator();
                //newPars = new Parser(myAlloc);
                break;
            default:
                myAlloc = new ContiguousAllocator(3);
                newPars = new Parser(myAlloc);
        }*/

        input = new Scanner(System.in);
        while(input.hasNext()){
            newPars.addCommand(input.nextLine());
        }
    }
}
