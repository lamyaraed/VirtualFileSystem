package com.company;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) {
        String path="root/";
        Parser newPars =  null;
        Scanner input = new Scanner(System.in);
        int Alloc;
        DiskAllocator myAlloc;
        System.out.println("How Would you like to allocate? \n 1.Contiguous Allocation \n 2.Indexed Allocation");
        Alloc = input.nextInt();
        System.out.println("How many blocks in your VFS?");
        int blocks=0;
        blocks = input.nextInt();
        switch (Alloc){
            case 1:
                myAlloc = new ContiguousAllocator(blocks);
                newPars = new Parser(myAlloc);
                break;
            case 2:
                //myAlloc = new IndexedAllocator(blocks);
                //newPars = new Parser(myAlloc);
                break;
        }

        input = new Scanner(System.in);
        while(input.hasNext()){
            System.out.print(path+"->");
            newPars.addCommand(input.nextLine());
        }
    }
}
