package com.wildbitsfoundry.etk4j.control;

import com.wildbitsfoundry.etk4j.math.complex.Complex;
import com.wildbitsfoundry.etk4j.math.linearalgebra.Matrix;
import com.wildbitsfoundry.etk4j.util.NumArrays;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class StateSpaceTest {

    @Test
    public void testStepResponse() {
        // Test letting step calculate the default times.
        double[] timePoints = {0.0, 0.0707070707070707, 0.1414141414141414, 0.2121212121212121, 0.2828282828282828,
                0.35353535353535354, 0.4242424242424242, 0.4949494949494949, 0.5656565656565656, 0.6363636363636364,
                0.7070707070707071, 0.7777777777777778, 0.8484848484848484, 0.9191919191919191, 0.9898989898989898,
                1.0606060606060606, 1.1313131313131313, 1.202020202020202, 1.2727272727272727, 1.3434343434343434,
                1.4141414141414141, 1.4848484848484849, 1.5555555555555556, 1.6262626262626263, 1.6969696969696968,
                1.7676767676767675, 1.8383838383838382, 1.909090909090909, 1.9797979797979797, 2.0505050505050506,
                2.121212121212121, 2.191919191919192, 2.2626262626262625, 2.333333333333333, 2.404040404040404,
                2.4747474747474745, 2.5454545454545454, 2.616161616161616, 2.686868686868687, 2.7575757575757573,
                2.8282828282828283, 2.898989898989899, 2.9696969696969697, 3.04040404040404, 3.111111111111111,
                3.1818181818181817, 3.2525252525252526, 3.323232323232323, 3.3939393939393936, 3.4646464646464645,
                3.535353535353535, 3.606060606060606, 3.6767676767676765, 3.7474747474747474, 3.818181818181818,
                3.888888888888889, 3.9595959595959593, 4.03030303030303, 4.101010101010101, 4.171717171717171,
                4.242424242424242, 4.313131313131313, 4.383838383838384, 4.454545454545454, 4.525252525252525,
                4.595959595959596, 4.666666666666666, 4.737373737373737, 4.808080808080808, 4.878787878787879,
                4.949494949494949, 5.02020202020202, 5.090909090909091, 5.161616161616162, 5.232323232323232,
                5.303030303030303, 5.373737373737374, 5.444444444444445, 5.515151515151515, 5.585858585858586,
                5.656565656565657, 5.727272727272727, 5.797979797979798, 5.8686868686868685, 5.9393939393939394,
                6.0101010101010095, 6.08080808080808, 6.151515151515151, 6.222222222222222, 6.292929292929292,
                6.363636363636363, 6.434343434343434, 6.505050505050505, 6.575757575757575, 6.646464646464646,
                6.717171717171717, 6.787878787878787, 6.858585858585858, 6.929292929292929, 7.0};

        double[] yOut = {1.0, 1.0706501935709256, 1.140974765048918, 1.210688931991135, 1.2795483108687806,
                1.3473447285119136, 1.4139024171549208, 1.4790745602287427, 1.542740158743866, 1.6048011905907762,
                1.6651800373688195, 1.7238171554557589, 1.780668969963074, 1.8357059719994613, 1.888911001299238,
                1.9402776977747203, 1.9898091069325015, 2.0375164253625098, 2.0834178736746027, 2.1275376853284267,
                2.169905200785859, 2.2105540573184874, 2.2495214656316778, 2.2868475652277396, 2.3225748511289344,
                2.356747665221635, 2.389411746070436, 2.4206138315896917, 2.450401309453764, 2.4788219105798044,
                2.505923441431484, 2.5317535512718585, 2.5563595308412403, 2.5797881392542408, 2.6020854562014053,
                2.62329675680727, 2.643466406740311, 2.6626377753929726, 2.6808531651534517, 2.698153754976823,
                2.7145795566328093, 2.7301693821624227, 2.7449608212170067, 2.758990227082093, 2.7722927103059356,
                2.7849021389595796, 2.7968511446527513, 2.808171133518515, 2.818892301460273, 2.8290436530279752,
                2.838653023356994, 2.8477471026635475, 2.856351462845387, 2.8644905857861644, 2.872187893006914,
                2.879465776348818, 2.886345629408285, 2.892847879478632, 2.8989920197827113, 2.904796641807921,
                2.910279467579388, 2.9154573817291034, 2.920346463238455, 2.9249620167493076, 2.929318603354615,
                2.9334300707937038, 2.937309582990024, 2.940969648880431, 2.9444221504950585, 2.9476783702557783,
                2.9507490174690503, 2.9536442539959573, 2.9563737190882944, 2.9589465533849477, 2.961371422067471,
                2.963656537177827, 2.965809679104774, 2.967838217248403, 2.969749129874896, 2.97154902317578,
                2.9732441495477375, 2.974840425110601, 2.976343446482329, 2.977758506830794, 2.9790906112229294,
                2.980344491292377, 2.9815246192471365, 2.9826352212389913, 2.9836802901165527, 2.9846635975837863,
                2.985588705785755, 2.9864589783431175, 2.987277590856656, 2.9880475409027722, 2.9887716575404877,
                2.98945261035007, 2.9900929180229268, 2.9906949565219074, 2.9912609668306436, 2.99179306231};

        Matrix A = new Matrix(new double[][]{
                {-2.0, -1.0},
                {1.0, 0.0}
        });
        Matrix B = new Matrix(new double[][]{
                {1.0},
                {0.0}
        });
        Matrix C = new Matrix(new double[][]{
                {1.0, 2.0}
        });
        Matrix D = new Matrix(new double[][]{
                {1.0}
        });

        StateSpace ss = new StateSpace(A, B, C, D);
        StepResponse sr = ss.step();

        assertArrayEquals(timePoints, sr.getTime(), 1e-12);
        assertArrayEquals(yOut, sr.getResponse(), 1e-12);

        // TODO test with initial conditions
    }

    @Test
    public void testTimeResponse() {
        double[] timePoints = NumArrays.linSpace(0.0, 15.0, 50);

        double[][] yOut = {
                {
                        0.5, 0.5953183651284722, 0.6466006894702705, 0.6678400703382454, 0.6698741971149202,
                        0.6603517271890003, 0.6443907578129624, 0.6252901540553092, 0.605107621477758,
                        0.5850810907482419, 0.565919422326563, 0.5479966196261058, 0.5314789160089058,
                        0.5164067084348589, 0.5027467403495963, 0.490424948793618, 0.4793468674583856,
                        0.46941008368454124, 0.4605116581220423, 0.4525523756703662,  0.4454390219550386,
                        0.43908544511906605, 0.4334128840366918, 0.42834986593368696, 0.4238318628798132,
                        0.41980082448268374, 0.41620465842136234, 0.41299670163470775, 0.4101352068950031,
                        0.4075828582364826,  0.40530632177284037, 0.4032758342356342, 0.40146482908330694,
                        0.3998495986242253, 0.39840898985800316, 0.39712413140877456, 0.3959781888388464,
                        0.3949561456888452, 0.39404460772721456, 0.3932316280682728, 0.3925065510100283,
                        0.39185987263630917, 0.3912831164143353, 0.39076872219426406, 0.3903099471794198,
                        0.3899007775842252, 0.38953584983146267, 0.3892103802620367, 0.3889201024397603,
                        0.3886612112318269
                },
                {
                        0.8, 0.9959680194245775, 1.1180304088529802, 1.1976399808454654, 1.251860778141964,
                        1.2903232256449435, 1.3186623815694913, 1.3402822921893738, 1.357297410991361,
                        1.3710543066850405, 1.3824308549397892, 1.3920128997166337, 1.400201072633966,
                        1.4072766134118206, 1.413442543124373, 1.4188497429727276, 1.423613656729683,
                        1.4278251048341055, 1.4315573681370581, 1.434870890584421, 1.437816451146913, 1.440437344143818,
                        1.442770911554792, 1.4448496473217785, 1.4467020151955503, 1.4483530717162212,
                        1.4498249539947368, 1.4511372715039312, 1.4523074279304307, 1.453350890640713,
                        1.4542814197960112, 1.4551112655413885, 1.4558513393116876, 1.4565113637052485,
                        1.4571000042965705, 1.4576249860129151, 1.4580931961725725, 1.458510775900445,
                        1.4588832013520632, 1.459215355959247, 1.459511594739158, 1.4597758015701556,
                        1.4600114402237927, 1.4602215998464878, 1.4604090355027548, 1.4605762043214523,
                        1.4607252977252627, 1.4608582701699495, 1.4609768647727284, 1.4610826361673501
                },
                {
                        0.0, 0.3768099867897473, 0.6863499079918728, 0.9454290628988934, 1.1656265007098252,
                        1.3550692931438462, 1.519592551454485, 1.6634947673206493, 1.7900301234411655,
                        1.901730500864768, 2.0006173966147127, 2.0883426581566686, 2.166283101943194,
                        2.2356051451027166, 2.297309827059671, 2.352264903733963, 2.4012283271341985,
                        2.444865903280223, 2.483764946175455, 2.5184451190681996, 2.5493672509785434,
                        2.5769406561086714, 2.6015293149407173, 2.6234571657244707, 2.643012682676774,
                        2.660452869131305, 2.676006761501208, 2.6898785177445728, 2.702250148523617, 2.7132839381525855,
                        2.723124594273006, 2.731901159030218, 2.7397287097347185, 2.746709873170971, 2.7529361745965293,
                        2.75848923987553, 2.763441866989991, 2.7678589812844008, 2.771798487162919, 2.775312027529688,
                        2.7784456610077894, 2.7812404658655017, 2.7837330785991843, 2.7859561742537426,
                        2.7879388947903143, 2.789707231124991, 2.791284363851997, 2.792690967121211, 2.7939454796556493,
                        2.795064346463005
                }
        };

        double[][] xOut = {
                {
                        0.0, 0.5267501056774089, 0.8911770818821702, 1.1590796544772357, 1.365918534205994,
                        1.5317851824549613, 1.6686504913182425, 1.7840029764980359, 1.8827485503050496,
                        1.9682438649699665, 2.0428809638186713, 2.108430190272375, 2.166247799361379, 2.217405201691893,
                        2.262771458575312, 2.303067196651333, 2.3389006875050913, 2.370792598589974, 2.399193429237348,
                        2.4244961451581477, 2.4470456050280265, 2.467145800903668, 2.4850655748413084,
                        2.5010432462655547, 2.5152904391162414, 2.527995304172768, 2.5393252712442638,
                        2.549429426190113, 2.5584405814564626, 2.5664770911914636, 2.573644449984233, 2.580036705909967,
                        2.5857377126062113, 2.590822240748619, 2.5953569660187803, 2.59940134812399, 2.6030084134179314,
                        2.6062254520359174, 2.6090946391006846, 2.6116535884082, 2.613935846021406, 2.6159713303505994,
                        2.617786724558402, 2.6194058264773092, 2.620849860654884, 2.6221377566350195,
                        2.6232863971346068, 2.6243108393761725, 2.6252245124825135, 2.626039393523876
                },
                {
                        0.0, -0.20211086962342162, -0.32889631386336166, -0.3996892365060543, -0.4323415453884835,
                        -0.4405064292273744, -0.4337714263251338, -0.41854458564292707, -0.3989630982031438,
                        -0.377619511041247, -0.3560871013639848, -0.3352809174120865, -0.31569850947057754,
                        -0.29757733820404664, -0.28099627369443214, -0.2659403062241015, -0.2523413616687342,
                        -0.2401037387679958, -0.2291197219678109, -0.21927895957920024, -0.21047391314685263,
                        -0.20260285207856113, -0.19557133183800582, -0.18929275041799393, -0.1836883581766988,
                        -0.17868695610503604, -0.17422442857353693, -0.1702432001764153, -0.16669167064897839,
                        -0.16352365943234318, -0.16069787747343223, -0.15817743520084823, -0.15592939034684092,
                        -0.15392433614324025, -0.15213602861078043, -0.15054105068252802, -0.14911851043380478,
                        -0.14784977053137532, -0.14671820603692043, -0.14570898782466493, -0.14480888905134864,
                        -0.14400611231848906, -0.14329013537407825, -0.14265157340302537, -0.14208205614705283,
                        -0.1415741182725264, -0.14112110156775962, -0.14071706769963965, -0.14035672039352584,
                        -0.140035336021149
                },
                {
                        0.0, 0.05737069683238586, 0.17847467461410368, 0.32004547914781684, 0.46296575579321514,
                        0.5990148742468686, 0.7249495534960456, 0.8398213876615431, 0.9437392762381387,
                        1.0372866477238163, 1.1212412835542942, 1.1964383907993745, 1.2637033872638288,
                        1.323819606671644, 1.3775139649761974, 1.4254520431843278, 1.4682381337598152,
                        1.5064178569945665, 1.5404820286186314, 1.5708710411923388, 1.597979346183877,
                        1.6221598092021452, 1.643727818823111, 1.6629650927531003, 1.6801231620853614,
                        1.6954265356983425, 1.7090755586540611, 1.721248984534999, 1.7321062842948607,
                        1.7417897147968897, 1.7504261696387573, 1.7581288336478993, 1.7649986609011352,
                        1.7711256944739477, 1.7765902444738706, 1.7814639393248946, 1.7858106637805597,
                        1.7896873957684243, 1.7931449529124563, 1.7962286584405966, 1.7989789351564598,
                        1.8014318352293535, 1.8036195127270385, 1.8055706450724858, 1.8073108089410659,
                        1.80886281552038, 1.8102470095241712, 1.8114815358778316, 1.8125825775700264, 1.8135645677874759
                }
        };

        Matrix A = new Matrix(new double[][]{
                {-1.5, -0.2, 1.0},
                {-0.2, -1.7, 0.6},
                {1.0, 0.6, -1.4}
        });
        Matrix B = new Matrix(new double[][]{
                {1.5, 0.6},
                {-1.8, 1.0},
                {0.0, 0.0}
        });
        Matrix C = new Matrix(new double[][]{
                {-0.0, -0.5, -0.1},
                {0.35, -0.1, -0.15},
                {0.65, 0.0, 0.6}
        });
        Matrix D = new Matrix(new double[][]{
                {0.5, 0.0},
                {0.05, 0.75},
                {0.0, 0.0}
        });
        double[][] U = new double[2][timePoints.length];
        for (int i = 0; i < 2; ++i) {
            U[i] = NumArrays.ones(timePoints.length);
        }
        StateSpace ss = new StateSpace(A, B, C, D);
        TimeResponse tr = ss.simulateTimeResponse(U, timePoints);

        assertArrayEquals(timePoints, tr.getTime(), 1e-12);

        assertArrayEquals(yOut[0], tr.getResponse()[0], 1e-12);
        assertArrayEquals(yOut[1], tr.getResponse()[1], 1e-12);
        assertArrayEquals(yOut[2], tr.getResponse()[2], 1e-12);

        double[][] transposedStateVector = NumArrays.transpose(tr.getEvolutionOfStateVector());

        assertArrayEquals(xOut[0], transposedStateVector[0], 1e-12);
        assertArrayEquals(xOut[1], transposedStateVector[1], 1e-12);
        assertArrayEquals(xOut[2], transposedStateVector[2], 1e-12);

        // TODO test with initial conditions
    }

    @Test
    public void testEvaluateAt() {
        double[][] A = {{-2, -1}, {1, 0}};
        double[][] B = {{1}, {0}};
        double[][] C = {{1, 2}};
        double[][] D = {{1}};

        StateSpace ss = new StateSpace(A, B, C, D);
        Complex[] out = ss.evaluateAt(100);
        assertEquals(1.0000000199960006	, out[0].real(), 1e-12);
        assertEquals(-0.010000999700049994, out[0].imag(), 1e-12);

        A = new double[][] {{-2, -1, 3}, {1, 0, 5}, {4, 5, 10}};
        B = new double[][]{{1, 2, 4}, {0, 6, 7}, {9, 10, 22}};
        C = new double[][]{{1, 2, 0}, {0, 1, 0}, {0, 0, 1}};
        D = new double[][]{{0, 0, 1}, {2, 3, 4}, {5, 6, 7}};

        ss = new StateSpace(A, B, C, D);
        Complex[] result = {new Complex(-0.011542373908342127, -0.008843720085419242),
                new Complex(1.9954643232641387, 4.86878158958262E-4),
                new Complex(4.990756640908159, -0.08875003943655803),
                new Complex(-0.012186972440090565, -0.1382853147258124),
                new Complex(2.9948877925087123, -0.0593012177526939),
                new Complex(5.986416866545381, -0.09830757398167314),
                new Complex(0.9725318695647737, -0.17664584358826158),
                new Complex(3.988777297177795, -0.06861654835210704),
                new Complex(6.973335512000304, -0.21657150706728753)};
        assertArrayEquals(result, ss.evaluateAt(100));
        TransferFunction[] tfs = ss.toTransferFunction(0);
        Arrays.stream(tfs).forEach(System.out::println);

        tfs = ss.toTransferFunction(1);
        Arrays.stream(tfs).forEach(System.out::println);

        tfs = ss.toTransferFunction(2);
        Arrays.stream(tfs).forEach(System.out::println);

        Arrays.stream(tfs).forEach(x -> System.out.println(x.evaluateAt(100)));

        System.out.println(Arrays.toString(ss.evaluateAt(100)));

        A = new double[][]{{0, 8}, {1, 3}};
        B = new double[][]{{0}, {1}};
        C = new double[][]{{1, 0}};
        D = new double[][]{{0}};

        ss = new StateSpace(A, B, C, D);
        System.out.println(ss.toTransferFunction());
    }
}
