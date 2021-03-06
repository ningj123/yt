<div id="Page_AccountFormView" class="row" data-role="page">
    <div class="col-lg-12">
        <div class="page-bar">
            <ul class="page-breadcrumb">
                <li>
                    <i class="icon-home"></i>
                    <a href="index.html">首页</a>
                    <i class="fa fa-angle-right"></i>
                </li>
                <li>
                    <span>账户编辑</span>
                </li>
            </ul>
        </div>
        <!-- END PAGE HEADER-->

        <div class="row">
            <div class="col-md-12">
                <!-- BEGIN SAMPLE FORM PORTLET-->
                <div class="portlet light ">
                    <div class="portlet-body">
                        <form id="accountForm" class="form-horizontal" role="form">
                            <input type="hidden" class="form-control" id="id" name="id">

                            <div class="form-group">
                                <label for="name" class="col-md-2 control-label">姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" name="realName" id="realName" placeholder="姓名">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="code" class="col-md-2 control-label">账号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" name="userName" id="userName" placeholder="账户">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="value" class="col-md-2 control-label">密码</label>
                                <div class="col-md-4">
                                    <input type="password" class="form-control" name="password" id="password" placeholder="密码"> </div>
                            </div>

                            <div class="form-group">
                                <label for="value" class="col-md-2 control-label">确认密码</label>
                                <div class="col-md-4">
                                    <input type="password" class="form-control" name="confirmPassword" id="confirmPassword" placeholder="确认密码"> </div>
                            </div>

                            <hr>
                            <div class="form-group">
                                <div class="col-md-12 pull-right">
                                    <button id="btnSave" type="button" class="btn red">保存</button>

                                    <button id="btnCancel" type="button" class="btn blue">取消</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>