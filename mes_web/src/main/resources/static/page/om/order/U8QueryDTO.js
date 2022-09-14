class U8QueryDTO {
    constructor(data) {
        if (data.productInvCode !== "" && data.productInvCode != null){
            this.cinvcode = data.productInvCode
        }
        if (data.invCode !== "" && data.invCode != null){
            this.cinvcode = data.invCode
        }
    }
}