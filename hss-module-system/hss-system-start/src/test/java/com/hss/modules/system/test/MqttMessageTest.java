package com.hss.modules.system.test;

import com.hss.HssSystemApplication;
import com.hss.modules.scada.process.ScadaDataHandlerService;
import com.hss.modules.util.ExpressionUtil;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = HssSystemApplication.class)
public class MqttMessageTest {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private ScadaDataHandlerService scadaDataHandlerService;
    @Autowired
    private ExpressionUtil expressionUtil;

//@Test
    public void test2(){
        Boolean aBoolean = expressionUtil.getValueByAttrValue("1==0&&(0==1||0==1)", Boolean.class);
        System.out.println(aBoolean);

    }
}
