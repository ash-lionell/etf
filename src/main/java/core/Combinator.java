package core;

import java.util.Arrays;

public class Combinator {
    public static void main(String args[]) {
        Combinator c=new Combinator(new int[]{1,2},new int[]{3,4},new int[]{5,6,7});
        //c.process();
        System.out.println(c.getNumOfCombinations());
        while (c.hasNext()) {
            c.test();
            System.out.println();
        }
    }

    int arrays[][];
    int numOfArrays;
    int numOfCombinations;
    int indices[];
    //int masterArrayIndex;
    int counter;

    public Combinator(int[]...arrays) {
        numOfArrays=arrays.length;
        this.arrays=new int[numOfArrays][];
        numOfCombinations=1;
        for (int i=0;i<numOfArrays;++i) {
            this.arrays[i]=arrays[i];
            numOfCombinations*=arrays[i].length;
        }
        indices=new int[numOfArrays];
        Arrays.fill(indices,0);
        counter=0;
    }

    void test() {
        int out[]=this.next();
        for(int p=0;p<numOfArrays;++p)
            System.out.print(out[p]);
    }

    public boolean hasNext() { return counter<numOfCombinations; }

    public int[] next() {
        if(!hasNext())
            return null;
        int output[]=getCurrentCombination();
        updateIndices();
        incrementCounter();
        return output;
    }

    public int getNumOfCombinations() { return numOfCombinations; }

    int[] getCurrentCombination() {
        int output[]=new int[numOfArrays];
        int j;
        for (int k=0;k<numOfArrays;++k) {
            //System.out.print(arrays[k][indices[k]]);
            output[k]=arrays[k][indices[k]];
        }
        return output;
    }

    void updateIndices() {
        for(int j=numOfArrays-1;j>=0;--j) {
            if(indices[j]<arrays[j].length-1) {
                indices[j]++;
                break;
            }
            else
                indices[j]=0;
        }
    }

    //void incrementMasterArrayIndex() { masterArrayIndex++; }
    //void decrementMasterArrayIndex() { masterArrayIndex--; }

    void incrementCounter() { counter++; }

    void process() {
        System.out.println("num of master arrays : "+numOfArrays);
        int output[];
        done: while(true) {
            for (int i=numOfArrays-1;i>=0;--i) {
                output=new int[numOfArrays];
                int j;
                for (int k=0;k<numOfArrays;++k) {
                    System.out.print(arrays[k][indices[k]]);
                    output[k]=arrays[k][indices[k]];
                }
                for(j=numOfArrays-1;j>=0;--j) {
                    if(indices[j]<arrays[j].length-1) {
                        indices[j]++;
                        break;
                    }
                    else
                        indices[j]=0;
                }
                if(j==-1) {
                    //done=true;
                    break done;
                }
                System.out.println(/*"j : "+j+indices[0]+indices[1]*/);
            }
        }
    }
}
