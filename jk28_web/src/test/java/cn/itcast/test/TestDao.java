package cn.itcast.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.domain.Dept;
import cn.itcast.domain.DictAndModule;
import cn.itcast.service.DeptService;
import cn.itcast.service.DictAndModuleService;
import cn.itcast.service.DictService;
@RunWith(SpringJUnit4ClassRunner.class) // 不能是PowerMock等别的class，否则无法识别spring的配置文件  
@ContextConfiguration("classpath:applicationContext.xml")   // 读取spring配置文件
public class TestDao {
	@Resource
	private DeptService deptService;
	@Resource
	private DictService dictService;
	
	@Resource
	private DictAndModuleService dictModule;
	@Test
	public void test01(){
	/*	ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		ctx.getBean("deptService");
		List<Dept> list = baseDao.find("from Dept", Dept.class, null);*/
		List<Dept> list = deptService.find("from Dept", Dept.class, null);
		for (Dept dept : list) {
			System.out.println(dept.getDeptName());
		}
	}
	
	@Test
	public void test02(){
	
//	    List<DictAndModule> list = dictModule.find("from DictAndModule", DictAndModule.class, null);
		List<DictAndModule> list = dictModule.find("from DictAndModule where moduleId = ?", DictAndModule.class, new String[]{"1"});
	
		for (DictAndModule d : list) {
			System.out.println(d.getDictId()+"  ---  "+d.getModuleId());
		}
	}
}
