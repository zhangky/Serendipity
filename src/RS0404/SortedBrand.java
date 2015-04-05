package RS0404;

class SortedBrand implements Comparable<SortedBrand>{
	int brandId;
	double weight;
	
	SortedBrand(int brandId,double weight){
		this.brandId = brandId;
		this.weight = weight;
	}
	
	public String toString(){
		return weight+"\t"+brandId+"\n";
	}

	@Override
	public int compareTo(SortedBrand other) {
		if(this.weight<other.weight) return 1;
		if(this.weight>other.weight) return -1;
		return 0;
	}
}
