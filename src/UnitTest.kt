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
        monarchHero = FakeMonarchFactory.createRandomHero() as MonarchHero
        for (i in 0..6) {
            heroes.add(FakeNonMonarchFactory.createRandomHero())
        }
        //play()
        assertTrue(monarchHero.dodgeAttack()) //CaoCao dodgeAttack
    }

    @Test
    fun testBeingAttacked() { //Test Doubles - Spy
        for (i in 0..6) {
            heroes.add(NonMonarchFactory.createRandomHero())
        }
        val hero = NonMonarchFactory.createRandomHero()
        val spy = object: WarriorHero(MinisterRole()) {
            override val name = hero.name
            override fun beingAttacked() {
                hero.beingAttacked()
                assertTrue(hero.hp >= 0)
            }
        }
        for(i in 0..10)
            spy.beingAttacked()
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