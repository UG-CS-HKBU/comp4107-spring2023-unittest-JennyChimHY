import org.junit.Assert.assertTrue
import org.junit.Test

class UnitTest {
//    @Test
//    fun test1() { //ascending order, create monarch
//        heroes.clear()
//        monarchHero = CaoCao()
//        heroes.add(monarchHero)
//        heroes.add(ZhangFei(MinisterRole()))
//        assertTrue(monarchHero.name == "Cao Cao")
//    }
//
//
//    @Test
//    fun test2() { //create non-monarch
//        if(heroes.size < 2) {
//            test1()
//        }
//        assertTrue(heroes.size == 2)
//    }

    @Test
    fun testCaoDodgeAttack() { //Test Doubles - Stub
        monarchHero = CaoCao()
        for (i in 0..6) {
            heroes.add(NonMonarchFactory.createRandomHero())
        }
        for (h in heroes) {
            h.beingAttacked()
        }

        assertTrue(monarchHero.dodgeAttack()) //CaoCao dodgeAttack
    }

    @Test
    fun testBeingAttacked() { //Test Doubles - Spy
        for (i in 0..6) {
            heroes.add(NonMonarchFactory.createRandomHero())
        }
        for (h in heroes) {
            val spy = object : WarriorHero(MinisterRole()) {
                override val name = h.name
                override fun beingAttacked() {
                    h.beingAttacked()
                    assertTrue(h.hp >= 0) //0 announce die no -1
                }
            }
            for (i in 0..9)
                spy.beingAttacked()
        }
    }

    class DummyRole : Role {
        override val roleTitle = "Dummy"
        override fun getEnemy() = "Dummy"
    }

    @Test
    fun testDiscardCards() { //Dummy
        val dummy = DummyRole()
        val hero = ZhangFei(dummy)
        hero.discardCards()
    }
}

class CaoCaoUnitTest { //Fake
    @Test
    fun testCaoDodgeAttack() { //Test Doubles - Stub
        monarchHero = FakeMonarchFactory.createRandomHero() as MonarchHero
        for (i in 0..6) {
            heroes.add(FakeNonMonarchFactory.createRandomHero())
        }
        //similar to play()
        for (h in heroes) {
            h.templateMethod()
            h.templateMethod()
        }
        assertTrue(monarchHero.dodgeAttack()) //CaoCao dodgeAttack
    }

    object FakeNonMonarchFactory: GameObjectFactory {
        var count = 0 //for SimaYi, XuChu and XiaHouyuan
        var last: WeiHero? = null
        init {
            monarchHero = CaoCao()
        }
        override fun getRandomRole(): Role =
            MinisterRole()
        override fun createRandomHero(): Hero {
            val hero = when(count++) {
                0->SimaYi(getRandomRole())
                1->XuChu(getRandomRole())
                else->XiaHouyuan(getRandomRole())
            }
            val cao = monarchHero as CaoCao
            if (last == null)
                cao.helper = hero
            else
                last!!.setNext(hero)
            last = hero
            return hero
        }
    }

    object FakeMonarchFactory: GameObjectFactory {
        override fun getRandomRole(): Role =
            MinisterRole() //not necessary at all
        override fun createRandomHero(): Hero {
            return CaoCao()
        }
    }

}