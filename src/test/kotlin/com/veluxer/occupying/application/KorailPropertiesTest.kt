package com.veluxer.occupying.application

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class KorailPropertiesTest(korailProperties: KorailProperties) : StringSpec({
    "host 속성은 코레일 호스트 주소를 반환한다" {
        korailProperties.host shouldBe "https://smart.letskorail.com"
    }

    "contextPath 속성은 코레일 API의 context path를 반환한다." {
        korailProperties.contextPath shouldBe "/classes/com.korail.mobile."
    }

    "timeout 속성은 코레일 API의 타임아웃 설정값을 반환한다." {
        korailProperties.timeout shouldBe 2000L
    }
})
