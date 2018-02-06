package cn.itcast.action.sysadmin;

import java.util.List;

import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.action.BaseAction;
import cn.itcast.domain.Module;
import cn.itcast.service.ModuleService;
import cn.itcast.service.RoleService;
import cn.itcast.util.Page;

/**
 * 部门管理的Action
 * 
 * @author LR-PC
 */
public class ModuleAction extends BaseAction implements ModelDriven<Module> {

	private static final long serialVersionUID = 1L;
	private Module model = new Module();

	@Override
	public Module getModel() {
		return model;
	}

	// 分页查询
	private Page<Module> page = new Page<Module>();

	public Page<Module> getPage() {
		return page;
	}

	public void setPage(Page<Module> page) {
		this.page = page;
	}

	// 注入ModuleService
	private ModuleService moduleService;
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	// 注入RoleService
	private RoleService roleService;
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * 分页查询
	 */
	public String list() throws Exception {
		// 此处的page = 可以写出来，也可不写，因为修改的引用 类型的
		page = moduleService.findPage("from Module", page, Module.class, null);
		// 设置分页的URL
		page.setUrl("moduleAction_list");

		// 将page对象压入栈顶
		super.push(page);
		return "list";
	}

	/**
	 * 查看
	 */
	public String toview() {
		// 1.调用业务方法，根据id,得到对象
		Module Module = moduleService.get(Module.class, model.getId());
		// 放入栈顶
		super.push(Module);
		return "toview";
	}

	/**
	 * 新增
	 */
	public String tocreate() {
		// 调用业务方法
		List<Module> moduleList = moduleService.find("from Module ", Module.class, null);
		// 将查询结果放入值栈中 单个对象用push，集合用下面这个
		super.put("moduleList", moduleList);
		// 跳页面
		return "tocreate";
	}

	/**
	 * 保存
	 */
	public String insert() {
		// 调用业务方法，实现保存
		moduleService.saveOrUpdate(model);
		// 跳页面
		return "alist";
	}

	/**
	 * 进入修改页面
	 */
	public String toupdate() {
		// 根据id，得到一个对象
		Module obj = moduleService.get(Module.class, model.getId());
		// 将对象放入值栈中
		super.push(obj);
		// 跳页面
		return "toupdate";
	}

	/**
	 * 修改
	 */
	public String update() {
		// 根据id，得到一个数据库中保存的对象
		Module obj = moduleService.get(Module.class, model.getId());
		// 设置修改的属性
		obj.setName(model.getName());
		obj.setLayerNum(model.getLayerNum());
		obj.setCpermission(model.getCpermission());
		obj.setCurl(model.getCurl());
		obj.setCtype(model.getCtype());
		obj.setState(model.getState());
		obj.setBelong(model.getBelong());
		obj.setCwhich(model.getCwhich());
		obj.setRemark(model.getRemark());
		obj.setOrderNo(model.getOrderNo());
		
		moduleService.saveOrUpdate(obj);
		return "alist";
	}

	/**
	 * 删除 model id:String类型 具有同名框的一组值如何封装数据 如果服务器端是String类型： Struts2默认采用 , 分割
	 * id:Integer类型：100 200 300 将会导致数据丢失，Struts2默认只保存最后一个 Integer[]id;
	 * {100,200,300}服务器端用数组来接受没有问题
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		String[] ids = model.getId().split(",");
		moduleService.delete(Module.class, ids);
		return "alist";
	}
}
