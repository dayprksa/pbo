package esaku;

public class pelanggaranData {
    
    private final Integer kode;
    private final Integer subKode;
    private final String jenisPelanggaran;
    private final Integer poin;
    private final String penanganan;
    
    public pelanggaranData(Integer kode, Integer subKode, String jenisPelanggaran, Integer poin, String penanganan){
        this.kode = kode;
        this.subKode = subKode;
        this.jenisPelanggaran = jenisPelanggaran;
        this.poin = poin;
        this.penanganan = penanganan;
    }
    
    public Integer getKode(){
        return kode;
    }
    public Integer getSubKode(){
        return subKode;
    }
    public String getJenisPelanggaran(){
        return jenisPelanggaran;
    }
    public Integer getPoin(){
        return poin;
    }
    public String getPenanganan(){
        return penanganan;
    }
}