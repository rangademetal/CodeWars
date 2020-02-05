interface Sort {
//    public void bubbleSort(int v[]);
    public void getArray(int v[]);

}

class SortAlgo implements Sort {

    public void swap(int v[],int i, int j) {
        int aux = v[i];
        v[i] = v[j];
        v[j] = aux;
    }
    public void bubbleSort(int v[]) {

    }
    public void getArray(int v[]) {
        for(int i=0 ; i <v.length;i++)
            System.out.print("["+v[i]+"] ");

    }
}
