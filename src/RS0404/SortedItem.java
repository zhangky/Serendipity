package RS0404;


class SortedItem implements Comparable<SortedItem>{
	int itemId;
	double weight;
	
	SortedItem(int brandId,double weight){
		this.itemId = brandId;
		this.weight = weight;
	}
	
	public String toString(){
		return weight+"\t"+itemId+"\n";
	}

	@Override
	public int compareTo(SortedItem other) {
		if(this.weight<other.weight) return 1;
		if(this.weight>other.weight) return -1;
		return 0;
	}
}
