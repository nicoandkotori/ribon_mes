/*
 * 分页查询DTO
 * @author mijiahao
 */
class PageFindDTO {

    constructor() {
        this.page = 1;
        this.limit = 10;
        this.query = null;
    }
    
    setPage(value){
        this.page = value;
    }
    
    getPage(){
        return this.page;
    }
    
    setLimit(value){
        this.limit = value;
    }

    getLimit(){
        return this.limit;
    }

    setQuery(value){
        this.query = value;
    }

    getQuery(){
        return this.query;
    }

}