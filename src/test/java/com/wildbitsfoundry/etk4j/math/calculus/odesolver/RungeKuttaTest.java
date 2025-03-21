package com.wildbitsfoundry.etk4j.math.calculus.odesolver;

import com.wildbitsfoundry.etk4j.math.function.BivariateFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class RungeKuttaTest {

    @Test
    public void testRungeKutta23() {
        BivariateFunction func = (t, x) -> -x;
        RungeKutta rungeKutta = new RungeKutta23(func, 0.0, 1.0, 10.0, 1.0, 0.001,
                Math.exp(-6), null);

        List<Double> tValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        while (rungeKutta.status != OdeSolverStatus.FINISHED) {
            rungeKutta.step();
            tValues.add(rungeKutta.t);
            yValues.add(rungeKutta.y[0]);
        }

        double[] tSolution = {0.0326443353286873, 0.35908768861556034, 0.9288876265576596, 1.6469479545357713,
                2.6156116724039244, 3.6156116724039244, 4.615611672403924, 5.615611672403924, 6.615611672403924,
                7.615611672403924, 8.615611672403924, 9.615611672403924, 10.0};

        double[] ySolution = {0.9678826930655442, 0.6978834512680837, 0.392003252913781, 0.1873926074634615,
                0.06540125734101757, 0.02180041911367253, 0.007266806371224182, 0.002422268790408061,
                8.074229301360204E-4, 2.691409767120068E-4, 8.971365890400227E-5, 2.9904552968000765E-5,
                2.033578448145516E-5};

        assertArrayEquals(tSolution, tValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution, yValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
    }

    @Test
    public void testRungeKutta23DefaultArguments() {
        BivariateFunction func = (t, x) -> -x;
        RungeKutta rungeKutta = new RungeKutta23(func, 0.0, 1.0, 10.0);

        List<Double> tValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        while (rungeKutta.status != OdeSolverStatus.FINISHED) {
            rungeKutta.step();
            tValues.add(rungeKutta.t);
            yValues.add(rungeKutta.y[0]);
        }

        double[] tSolution = {0.0215515259567983, 0.23706678552478128, 0.591833281296728, 0.9705089040594623,
                1.354049888624263, 1.7387041763728368, 2.123749057436607, 2.5091093358711727, 2.8948795280344655,
                3.2812412374116464, 3.6684702647330205, 4.056973736383266, 4.447350094620666, 4.840478142750021,
                5.23764768003156, 5.640750979126159, 6.052564450577668, 6.4771675038079435, 6.920581308986186,
                7.391793137494477, 7.904549884328539, 8.480956192082468, 9.160296183728477, 10.0};

        double[] ySolution = {0.9786790398454016, 0.7888542777528701, 0.5527670441882453, 0.3780771501848815,
                0.25732213518840485, 0.17493779475625215, 0.1188825591147381, 0.08076324260369858, 0.054843962117137296,
                0.03722060047482315, 0.02523805610928566, 0.017090981348152675, 0.011551885603907474,
                0.00778620750054413, 0.005226573191229517, 0.003487305388762527, 0.002306300702296658,
                0.0015055124340541638, 9.640755667648614E-4, 6.00012134644003E-4, 3.577476576170869E-4,
                1.995508788829822E-4, 9.96075239654962E-5, 4.1254238032726717E-5};

        assertArrayEquals(tSolution, tValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution, yValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
    }

    @Test
    public void testRungeKutta23MultipleInitialConditions() {
        OdeSystemOfEquations odeSystemOfEquations = (t, y) -> {
            double dxdt = y[0] - y[1];
            double dydt = y[0] + y[1];
            return new double[] {dxdt, dydt};
        };
        RungeKutta rungeKutta = new RungeKutta23(odeSystemOfEquations, 0.0, new double[] {1, 0}, 10.0, 1.0, 0.001,
                Math.exp(-6), null);

        List<Double> tValues = new ArrayList<>();
        List<Double> yValues0 = new ArrayList<>();
        List<Double> yValues1 = new ArrayList<>();
        while (rungeKutta.status != OdeSolverStatus.FINISHED) {
            rungeKutta.step();
            tValues.add(rungeKutta.t);
            yValues0.add(rungeKutta.y[0]);
            yValues1.add(rungeKutta.y[1]);
        }

        double[] tSolution = {0.025976025597427347, 0.28573628157170083, 0.6423974632107396, 0.9415602755161425,
                1.2407230878215454, 1.517279443855995, 1.7715083511313474, 2.021787100256841, 2.29097421924673,
                2.5514070945230576, 2.7905309430240197, 3.0126104081129306, 3.1857583889400614, 3.358906369767192,
                3.563997453026649, 3.8057525582706306, 4.05090314219944, 4.277521220595271, 4.489607069883245,
                4.6577675037614314, 4.825927937639618, 4.9780007440115925, 5.182210064824636, 5.421904791058065,
                5.660287066661956, 5.88084070681308, 6.086840762766752, 6.245191922304215, 6.403543081841679,
                6.552534026960952, 6.7548446113692995, 6.993402989396838, 7.230866785247203, 7.450639053591104,
                7.655946482400699, 7.813594830473213, 7.971243178545727, 8.117344093508647, 8.317274652391955,
                8.554840682474776, 8.792688011927234, 9.0128248507089, 9.218633356666684, 9.377736872424684,
                9.52089620353776, 9.65618216873147, 9.846268246481957, 10.0};

        double[] ySolution0 = {1.0259701831225174, 1.2776035178859115, 1.5267550915211654, 1.5163127433229293,
                1.129856646197454, 0.25140486100338855, -1.1706776106990222, -3.3020115943796675, -6.552776469076043,
                -10.725013995834031, -15.41073920810532, -20.33853485998865, -24.373766691221242, -28.34124295452492,
                -32.52272281611676, -35.79963073333264, -35.74685313257538, -30.82683129280548, -20.100592247483277,
                -6.0696363053903575, 13.98169102504604, 38.18775616304511, 81.13082982480888, 148.7128818678593,
                235.66673642208997, 333.23109350974744, 436.8143851935606, 521.6862994454893, 607.532293124006,
                685.0106992865498, 775.6835225602056, 839.344527619152, 820.8462522725175, 690.0179629245631,
                428.2482878009952, 109.03692017439732, -334.9230218116534, -875.5305695946774, -1845.669436294149,
                -3387.276696489381, -5385.505416681393, -7632.654713679352, -10026.323060984238, -12004.132180751249,
                -13810.64538648963, -15469.332061476902, -17554.20169595952, -18838.970153323444};

        double[] ySolution1 = {0.02665662197817555, 0.37515343355827274, 1.1407960593646902, 2.078914915154872,
                3.2851597315719747, 4.577379565118072, 5.797551084526619, 6.845814193180313, 7.494510471488281,
                7.212614936750099, 5.6777033023639545, 2.679156690185075, -1.0281111935324643, -6.197521909276459,
                -14.536889066248099, -27.899308603442968, -45.70545510166953, -65.96119404243086, -87.7634441762113,
                -106.36305972168594, -125.28245468783436, -141.72167429578568, -160.76108836518648, -174.0763962466806,
                -170.21429656456093, -142.86326107330848, -88.1191636942311, -21.254931925323348, 71.78889956029016,
                187.07842083163416, 392.92284921345833, 717.5800846971679, 1135.1666470317791, 1603.578622691247,
                2100.920783997546, 2508.0988359639473, 2920.2626567826746, 3287.450183903881, 3723.5799897544484,
                4040.652354197807, 3971.1180706908385, 3365.5047092084988, 2132.032837865074, 605.8951467207048,
                -1284.2212340228366, -3589.4499602024716, -7798.406381055847, -12122.588618481495};

        assertArrayEquals(tSolution, tValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution0, yValues0.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution1, yValues1.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
    }

    @Test
    public void testRungeKutta23MultipleInitialConditionsDefaultConditions() {
        OdeSystemOfEquations odeSystemOfEquations = (t, y) -> {
            double dxdt = y[0] - y[1];
            double dydt = y[0] + y[1];
            return new double[] {dxdt, dydt};
        };
        RungeKutta rungeKutta = new RungeKutta23(odeSystemOfEquations, 0.0, new double[] {1, 0}, 10.0);

        List<Double> tValues = new ArrayList<>();
        List<Double> yValues0 = new ArrayList<>();
        List<Double> yValues1 = new ArrayList<>();
        while (rungeKutta.status != OdeSolverStatus.FINISHED) {
            rungeKutta.step();
            tValues.add(rungeKutta.t);
            yValues0.add(rungeKutta.y[0]);
            yValues1.add(rungeKutta.y[1]);
        }

        double[] tSolution = {9.990005004983772E-4, 0.010989005505482149, 0.0779045954964266, 0.20598345447103777,
                0.3890429503175797, 0.6178117956057783, 0.8591517663108597, 1.0849872333714907, 1.2958280043578703,
                1.4662156329737068, 1.5977801115034211, 1.7293445900331355, 1.8945346802131429, 2.110521237484493,
                2.3522151201801647, 2.58439501002676, 2.799866376383953, 2.9792937968703797, 3.128127899762949,
                3.2769620026555186, 3.4321181123238076, 3.6399516453367613, 3.8801064431148156, 4.115390906280105,
                4.3332981174869385, 4.536385527955039, 4.688520669712226, 4.8406558114694125, 4.992490839930133,
                5.197485166390288, 5.436823378324126, 5.673056879710124, 5.89175778412449, 6.095821225711974,
                6.250562234599588, 6.405303243487202, 6.554172089460422, 6.756587156194601, 6.995065061765902,
                7.232105587047631, 7.451509569496405, 7.6564353212583685, 7.813395708008026, 7.970356094757684,
                8.116373144405737, 8.316275735256161, 8.55380997573747, 8.791584762184673, 9.011658002131936,
                9.217403225771266, 9.3764064134945, 9.519494383779334, 9.654714309533832, 9.844752008791755, 10.0};

        double[] ySolution0 = {1.0009990001681635, 1.010988562397209, 1.077744181373405, 1.2028111656970648,
                1.3655956413437806, 1.5129657449019387, 1.5439521330663895, 1.3843927917318977, 0.9948222457595507,
                0.4544600705800317, -0.13201359127423506, -0.8898662619375476, -2.117354027469323, -4.2479252159434076,
                -7.41799686195886, -11.281780524947079, -15.541717632973038, -19.483119550603362, -22.912584469319388,
                -26.353165989040598, -29.764526450502757, -33.60632150200749, -35.99999890110011, -34.66149141532983,
                -28.41148345365239, -16.523729083333258, -2.7137118759048118, 16.156912694489037, 40.81633322844292,
                84.65402249678137, 153.01995220666092, 239.86832800644254, 336.88641711792036, 439.35723995196577,
                521.8930389505327, 605.192600883084, 681.974227497632, 771.7304223376287, 834.1310408734572,
                814.5417293287467, 683.3232325929239, 422.18945193423, 105.35632566868128, -334.47275908528275,
                -871.6285391841691, -1835.8018881114103, -3367.7144681918826, -5352.7343633680775, -7584.760260440518,
                -9962.02507485999, -11925.489436734086, -13719.066516391904, -15365.896404129313, -17436.4639668071,
                -18723.063025159587};

        double[] ySolution1 = {9.999988348332058E-4, 0.011110206084778662, 0.08413131157922513, 0.2513183940492778,
                0.5597633907794669, 1.0749102383826878, 1.7896433731698427, 2.6202883010528364, 3.5223186953273142,
                4.316697676625522, 4.949347107793618, 5.576994805843444, 6.317262708468798, 7.097283006746366,
                7.4844153642729445, 7.037776685136297, 5.538436975846952, 3.202630984264585, 0.3230260192658596,
                -3.5721560833608788, -8.878026831650637, -18.25741891992307, -32.72183167264474, -50.89815943144157,
                -71.13207477919028, -92.40065629399112, -109.26526763291248, -126.23895217201304, -142.4146354836392,
                -161.00035735053672, -173.30109781929238, -168.11871434734695, -139.53150578563637, -83.88052926404698,
                -17.707151640708275, 73.4741444352509, 188.50346997611592, 394.0071938237103, 717.6071310889033,
                1132.8176158569258, 1598.1659981100559, 2091.7734468319713, 2494.615258741438, 2902.2236954658683,
                3266.668968522233, 3699.593223987552, 4013.99795254541, 3944.190360497806, 3341.931799669795,
                2116.1971659193205, 600.6409647640216, -1276.2052952657966, -3564.9647803225953, -7744.468021233967,
                -12086.09814713661};

        assertArrayEquals(tSolution, tValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution0, yValues0.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution1, yValues1.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
    }

    @Test
    public void testRungeKutta45() {
        BivariateFunction func = (t, x) -> -x;
        RungeKutta rungeKutta = new RungeKutta45(func, 0.0, 1.0, 10.0, 1.0, 0.001,
                Math.exp(-6), null);

        List<Double> tValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        while (rungeKutta.status != OdeSolverStatus.FINISHED) {
            rungeKutta.step();
            tValues.add(rungeKutta.t);
            yValues.add(rungeKutta.y[0]);
        }

        double[] tSolution = {0.1283171479634216, 1.1283171479634215, 2.1283171479634215, 3.1283171479634215,
                4.128317147963422, 5.128317147963422, 6.128317147963422, 7.128317147963422, 8.128317147963422,
                9.128317147963422, 10.0};

        double[] ySolution = {0.879574381033538, 0.3239765636806864, 0.11933136762238628, 0.043953720407578944,
                0.01618962035012491, 0.005963176828962677, 0.002196436798667919, 8.090208875093502E-4,
                2.9798936023261037E-4, 1.097594143523445E-4, 4.5927433621121034E-5};

        assertArrayEquals(tSolution, tValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution, yValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
    }

    @Test
    public void testRungeKutta45DefaultConditions() {
        BivariateFunction func = (t, x) -> -x;
        RungeKutta rungeKutta = new RungeKutta45(func, 0.0, 1.0, 10.0);

        List<Double> tValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        while (rungeKutta.status != OdeSolverStatus.FINISHED) {
            rungeKutta.step();
            tValues.add(rungeKutta.t);
            yValues.add(rungeKutta.y[0]);
        }

        double[] tSolution = {0.10001999200479661, 1.0318648679261533, 1.9076513619732283, 2.787202775471218,
                3.6672072152941473, 4.548834361288937, 5.434289353064915, 6.328718602533393, 7.243493017781799,
                8.201472966165275, 9.242089962862652, 10.0};

        double[] ySolution = {0.9048193290005142, 0.3566043492765856, 0.1486078088286655, 0.06169755426780149,
                0.025603438983790867, 0.01060783482336087, 0.004378240134691273, 0.0017909836339876258,
                7.179428101420958E-4, 2.756992736494285E-4, 9.754820583728779E-5, 4.5723138338181306E-5};

        assertArrayEquals(tSolution, tValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution, yValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
    }

    @Test
    public void testRungeKutta45MultipleInitialConditions() {
        OdeSystemOfEquations odeSystemOfEquations = (t, y) -> {
            double dxdt = y[0] - y[1];
            double dydt = y[0] + y[1];
            return new double[] {dxdt, dydt};
        };
        RungeKutta rungeKutta = new RungeKutta45(odeSystemOfEquations, 0.0, new double[] {1, 0}, 10.0, 1.0, 0.001,
                Math.exp(-6), null);

        List<Double> tValues = new ArrayList<>();
        List<Double> yValues0 = new ArrayList<>();
        List<Double> yValues1 = new ArrayList<>();
        while (rungeKutta.status != OdeSolverStatus.FINISHED) {
            rungeKutta.step();
            tValues.add(rungeKutta.t);
            yValues0.add(rungeKutta.y[0]);
            yValues1.add(rungeKutta.y[1]);
        }

        double[] tSolution = {0.11187772386872015, 1.007930843193444, 1.999271727606395, 2.8448211368811007,
                3.684921261666479, 4.568816163110601, 5.382471317367101, 6.232827184027202, 7.049004403889039,
                7.886515731920654, 8.711704819712274, 9.539576468671115, 10.0};

        double[] ySolution0 = {1.1113842514617591, 1.4610914926227114, -3.069122887043963, -16.429902353989913,
                -34.035760621658426, -13.677393049997438, 135.0117027757588, 507.3684317131492, 827.0322316967461,
                -89.79023363872113, -4584.94515450421, -13758.728742928337, -18401.94942485325};

        double[] ySolution1 = {0.12486051574905874, 2.3166396846717032, 6.70775547689553, 5.011061651185726,
                -20.594892705054463, -95.27367878475552, -170.01846262271692, -24.983562453576752, 797.212040125511,
                2651.9899373783214, 3954.3666464557764, -1606.8576979577356, -11969.226607049524};

        assertArrayEquals(tSolution, tValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution0, yValues0.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution1, yValues1.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
    }

    @Test
    public void testRungeKutta45MultipleInitialConditionsDefaultConditions() {
        OdeSystemOfEquations odeSystemOfEquations = (t, y) -> {
            double dxdt = y[0] - y[1];
            double dydt = y[0] + y[1];
            return new double[] {dxdt, dydt};
        };
        RungeKutta rungeKutta = new RungeKutta45(odeSystemOfEquations, 0.0, new double[] {1, 0}, 10.0);

        List<Double> tValues = new ArrayList<>();
        List<Double> yValues0 = new ArrayList<>();
        List<Double> yValues1 = new ArrayList<>();
        while (rungeKutta.status != OdeSolverStatus.FINISHED) {
            rungeKutta.step();
            tValues.add(rungeKutta.t);
            yValues0.add(rungeKutta.y[0]);
            yValues1.add(rungeKutta.y[1]);
        }

        double[] tSolution = {9.990005004983772E-4, 0.010989005505482149, 0.11088905555531987, 0.6192045497806424,
                1.382089669508035, 2.1886745264776577, 3.0409220232001117, 3.8501444997495904, 4.692302440222046,
                5.510387390403902, 6.344081991514867, 7.1729079897274906, 7.997421142566754, 8.83984498857466,
                9.653412819353544, 10.0};

        double[] ySolution0 = {1.0009990001679976, 1.010988560732053, 1.1104087852762166, 1.5125820051873267,
                0.7470111209360931, -5.167845858987864, -20.804657732388993, -35.64746907319375, -2.1524508872690618,
                176.83697390542918, 567.0902130903308, 818.6482261522492, -425.9739197360036, -5744.841661123295,
                -15126.160705018545, -18422.824726566276};

        double[] ySolution1 = {9.999988348331724E-4, 0.01111020607978034, 0.12363936767105335, 1.0780116837890694,
                3.911819597071353, 7.270270005658339, 2.0977357114784674, -30.56513520016031, -108.95380182618516,
                -172.29763512402243, 34.85277945958623, 1011.2949360599736, 2936.0785250135714, 3798.42024655396,
                -3532.6044379477817, -11965.423911921906};

        assertArrayEquals(tSolution, tValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution0, yValues0.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution1, yValues1.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
    }

    @Test
    public void testRungeKutta45MultipleInitialConditionsPassingInitialStep() {
        OdeSystemOfEquations odeSystemOfEquations = (t, y) -> {
            double dxdt = y[0] - y[1];
            double dydt = y[0] + y[1];
            return new double[] {dxdt, dydt};
        };

        RungeKutta rungeKutta = new RungeKutta45(odeSystemOfEquations, 0, new double[] {1, 0}, 10.0,
                Double.POSITIVE_INFINITY, 0.001, 1.0E-6, 9.990005004983772E-4);

        List<Double> tValues = new ArrayList<>();
        List<Double> yValues0 = new ArrayList<>();
        List<Double> yValues1 = new ArrayList<>();
        while (rungeKutta.status != OdeSolverStatus.FINISHED) {
            rungeKutta.step();
            tValues.add(rungeKutta.t);
            yValues0.add(rungeKutta.y[0]);
            yValues1.add(rungeKutta.y[1]);
        }

        double[] tSolution = {9.990005004983772E-4, 0.010989005505482149, 0.11088905555531987, 0.6192045497806424,
                1.382089669508035, 2.1886745264776577, 3.0409220232001117, 3.8501444997495904, 4.692302440222046,
                5.510387390403902, 6.344081991514867, 7.1729079897274906, 7.997421142566754, 8.83984498857466,
                9.653412819353544, 10.0};

        double[] ySolution0 = {1.0009990001679976, 1.010988560732053, 1.1104087852762166, 1.5125820051873267,
                0.7470111209360931, -5.167845858987864, -20.804657732388993, -35.64746907319375, -2.1524508872690618,
                176.83697390542918, 567.0902130903308, 818.6482261522492, -425.9739197360036, -5744.841661123295,
                -15126.160705018545, -18422.824726566276};

        double[] ySolution1 = {9.999988348331724E-4, 0.01111020607978034, 0.12363936767105335, 1.0780116837890694,
                3.911819597071353, 7.270270005658339, 2.0977357114784674, -30.56513520016031, -108.95380182618516,
                -172.29763512402243, 34.85277945958623, 1011.2949360599736, 2936.0785250135714, 3798.42024655396,
                -3532.6044379477817, -11965.423911921906};

        assertArrayEquals(tSolution, tValues.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution0, yValues0.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
        assertArrayEquals(ySolution1, yValues1.stream().mapToDouble(Double::doubleValue).toArray(), 1e-12);
    }
}
