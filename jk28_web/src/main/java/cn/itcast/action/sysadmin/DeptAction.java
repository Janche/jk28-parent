package cn.itcast.action.sysadmin;

import java.util.List;

import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.action.BaseAction;
import cn.itcast.domain.Dept;
import cn.itcast.service.DeptService;
import cn.itcast.util.Page;
/**
 * 部门管理的Action
 * @author LR-PC
 */
public class DeptAction extends BaseAction implements ModelDriven<Dept>{
	
	private static final long serialVersionUID = 1L;
	private Dept model = new Dept();
	@Override
	public Dept getModel() {
		return model;
	}
	
	//分页查询
	private Page<Dept> page = new Page<Dept>();
	public Page<Dept> getPage() {
		return page;
	}

	public void setPage(Page<Dept> page) {
		this.page = page;
	}
	
	// 注入DeptService
	private DeptService deptService;
	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}
	
//	@Resource
//	private DictAndModuleService dictModuleService;
	
	/**
	 * 分页查询
	 */
	public String list() throws Exception {
		// 此处的page = 可以写出来，也可不写，因为修改的引用 类型的
		page = deptService.findPage("from Dept", page, Dept.class, null);
		//设置分页的URL
		page.setUrl("deptAction_list");
	//	dictModuleService.find("from DictAndModule where moduleId = ?", DictAndModule.class, new String[]{module.getId()});
		// 将page对象压入栈顶
		super.push(page);
		return "list";
	}
	
	/**
	 * 查看
	 */
	public String toview(){
		//1.调用业务方法，根据id,得到对象
		Dept dept = deptService.get(Dept.class, model.getId());
		// 放入栈顶
		super.push(dept);
		return "toview";
	}
	
	/**
	 * 新增
	 */
	public String tocreate(){
		// 调用业务方法
		List<Dept> deptList = deptService.find("from Dept where state=1", Dept.class, null);
		// 将查询结果放入值栈中 单个对象用push，集合用下面这个
		super.put("deptList", deptList);
		//跳页面
		return "tocreate";
	}
	
	/**
	 * 保存
	 * <input type="text" name="deptName" value=""/>
	 */
	public String insert(){
		//调用业务方法，实现保存
		deptService.saveOrUpdate(model);
		// 跳页面
		return "alist";
	}
	
	/**
	 * 进入修改页面
	 */
	public String toupdate(){
		// 根据id，得到一个对象
		Dept obj = deptService.get(Dept.class, model.getId());
		// 将对象放入值栈中
		super.push(obj); 
		// 查询父部门
		List<Dept> deptList = deptService.find("from Dept where state=1", Dept.class, null);
		deptList.remove(obj);
		super.put("deptList", deptList);
		// 跳页面
		return "toupdate";
	}
	
	/**
	 * 修改
	 */
	public String update(){
		// 根据id，得到一个数据库中保存的对象
		Dept obj = deptService.get(Dept.class, model.getId());
		// 设置修改的属性
		obj.setParent(model.getParent());
		obj.setDeptName(model.getDeptName());
		deptService.saveOrUpdate(obj);
		// 跳页面
		return "alist";
	}
	
	/**
	 * 删除
	 * model
	 * 	id:String类型
	 * 	具有同名框的一组值如何封装数据
	 * 	如果服务器端是String类型：
	 * 		Struts2默认采用 , 分割
	 * 	id:Integer类型：100  200  300
	 * 		将会导致数据丢失，Struts2默认只保存最后一个
	 * 	Integer[]id;	{100,200,300}服务器端用数组来接受没有问题
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		String[] ids = model.getId().split(",");
		deptService.delete(Dept.class, ids);
		return "alist";
	}
}
