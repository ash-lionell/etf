package core;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Permutator {
    //Prints the array
    void printArr(int a[], int n)
    {
        for (int i=0; i<n; i++)
            System.out.print(a[i] + " ");
        System.out.println();
    }

    //Generating permutation using Heap Algorithm
    void heapPermutation(int a[], int size, int n)
    {
        // if size becomes 1 then prints the obtained
        // permutation
        if (size == 1) {
            //printArr(a,n);
            //output[outputCounter++]=a.clone();
            output.add(a.clone());
        }

        for (int i=0; i<size; i++)
        {
            heapPermutation(a, size-1, n);

            // if size is odd, swap first and last
            // element
            if (size % 2 == 1)
            {
                int temp = a[0];
                a[0] = a[size-1];
                a[size-1] = temp;
            }

            // If size is even, swap ith and last
            // element
            else
            {
                int temp = a[i];
                a[i] = a[size-1];
                a[size-1] = temp;
            }
        }
    }

    int array[];
    //int output[][];
    ArrayList<int []> output;
    int outputCounter;

    public Permutator(int numOfObjects) {
        array = IntStream.rangeClosed(1,numOfObjects).toArray();
        //output=new int[(int) Math.pow(2,numOfObjects)-2][];
        output=new ArrayList<>();
        outputCounter=0;
    }

    public int[][] permutate() {
        heapPermutation(array,array.length,array.length);
        int t[][]=new int[output.size()][];
        return output.toArray(t);
        //return output;
    }

    // Driver code
    public static void main(String args[])
    {
        /*Permutator obj = new Permutator(3);
        int a[] = {1,2,3};
        obj.heapPermutation(a, a.length, a.length);*/
        Permutator p=new Permutator(4);
        p.permutate();
        for (int innera[]:p.output) {
            for (int inner:innera)
                System.out.print(inner+" ");
            System.out.println();
        }
    }
}
