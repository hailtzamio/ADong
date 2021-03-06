package com.elcom.com.quizupapp.ui.network

/**
 * Created by admin on 3/8/2018.
 */
class ConstantAPI {
    class BASE_URL {
        val bar ="http://192.168.28.27/e-quiz/public/api/"
    }

}

enum class Team(val type: String) {
    ADONG("ADONG"),
    CONTRACTOR("CONTRACTOR"),
    STOCK("STOCK"),
}

enum class PROJECTTYPE(val type: String) {
    NEW("NEW"),
    PRROCESSING("PRROCESSING"),
    PAUSED("PAUSED"),
    DONE("DONE")
}

enum class UserPermission(val type: String) {
    ProductRequirementr("ProductRequirementr"),
    ProductRequirementc("ProductRequirementc"),
    ProductRequirementu("ProductRequirementu"),
    Tripr("Tripr"),
    Tripc("Tripc"),
    TransportReqr("TransportReqr"),
    TransportReqc("TransportReqc")
}

enum class UserRoles(val type: String) {
    CarManagement("Quản lý đội xe"),
    Driver("Tài xế"),
    Secretary("Thư ký"),
    TeamLeader("Đội trưởng"),
    LorryManager("Quản lý đội xe-LORRY_MANAGER"),
    WareHouseKeeper("Thủ kho-WAREHOUSE_KEEPER"),
    FactoryManager("Quản lý sản xuất-FACTORY_MANAGER"),
    Contractor("Nhà thầu phụ-CONTRACTOR"),
    Admin("Administrator-ADMIN")
}

enum class AppColor(val hex: String) {
    Green("#3ca150"),
    Blue("#3366CC"),
    Gray("#90928E"),
    Orange("#FB9214"),
    Red("#962E34")
}