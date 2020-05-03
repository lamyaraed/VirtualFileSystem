import java.util.Scanner;

import AllocationManagement.ContiguousAllocator;
import AllocationManagement.DiskAllocator;
import AllocationManagement.IndexedAllocator;
import ParserManagement.Parser;

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
                myAlloc = new IndexedAllocator(blocks);
                newPars = new Parser(myAlloc);
                break;
        }
        String command;
        input = new Scanner(System.in);
        System.out.print(path+"->");
        while(input.hasNext()){
            command = input.nextLine();
            newPars.addCommand(command);
            if(command.equals("exit"))break;
            System.out.print(path+"->");
        }


    }
}
