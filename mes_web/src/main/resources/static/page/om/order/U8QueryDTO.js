class U8QueryDTO {
    constructor(data) {
        if (data.invCode !== "" && data.invCode != null){
            this.cinvcode = data.invCode
        }
    }
}