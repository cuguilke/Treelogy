package com.payinekereg.treelogy.constants;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.payinekereg.treelogy.R;

/**
 * Created by Emre on 3/14/2016.
 */
public class MyConstants
{
    public static final String      MY_CONSTANTS        = "my_constants"        ;
    public static final String      IS_FIRST_ENTRANCE   = "is_first_entrance"   ;
    public static final String      LANGUAGE            = "language"            ;

    public static final int         NEW_OBSERVATION     = 0                     ;
    public static final int         MY_OBSERVATIONS     = 2                     ;
    public static final int         TREES_AND_LEAVES    = 1                     ;

    public static final String      EXTRA               = "extra"               ;

    public static final String      ID                  = "id"                  ;

    public static final int         REQUEST_CAMERA      = 0                     ;
    public static final int         SELECT_FILE         = 1                     ;


    public static String[] leaves_tr = {

            "adi_gurgen",                   "adi_simsir",               "ak_kavak",         "akdeniz_defnesi",          "alic",
            "altuni_fener_agaci",           "anadolu_kestanesi",        "armut",            "at_kestanesi",             "ayva",
            "badem",                        "beyaz_dut",                "ceviz",            "cin_at_kestanesi",         "cin_erguvani",
            "cin_tarcini",                  "cinar_yaprakli_dut",       "dag_akcaagaci",    "disbudak",                 "duvar_sarmasigi",
            "elma",                         "erguvan",                  "findik",           "ihlamur",                  "incir",
            "indigo_agaci",                 "japon_akcaagaci",          "kafur_agaci",      "kara_kavak",               "karayemis",
            "kartopu_yaprakli_akcaagac",    "kayisi",                   "kermes_mesesi",    "kiraz",                    "kis_tatlisi",
            "kizilcik",                     "kus_igdesi",               "leylak",           "mabet_agaci",              "mandalina",
            "manolya",                      "mum_cicegi",               "nar",              "osmanthus_fragrans",       "ova_akcaagaci",
            "oya_agaci",                    "parlak_yaprakli_kurtbagri","pirnal_mese",      "salkim_sogut",             "seftali",
            "sus_erigi",                    "sus_kirazi",               "titrek_kavak",     "tuylu_mese",               "uc_disli_akcaagac",
            "yildiz_calisi",                "zakkum"
    };

    public static String[] leaves_tr_shown = {

            "Adi gürgen",                   "Adi şimşir",                   "Ak kavak",         "Akdeniz defnesi",      "Alıç",
            "Altuni fener ağacı",           "Anadolu kestanesi",            "Armut",            "At kestanesi",         "Ayva",
            "Badem",                        "Beyaz dut",                    "Ceviz",            "Çin at kestanesi",     "Çin erguvanı",
            "Çin tarçını",                  "Çınar yapraklı dut",           "Dağ akçaağacı",    "Dişbudak",             "Duvar sarmasığı",
            "Elma",                         "Erguvan",                      "Fındık",           "Ihlamur",              "İncir",
            "İndigo ağacı",                 "Japon akcaağacı",              "Kafur ağacı",      "Kara kavak",           "Karayemiş",
            "Kartopu yapraklı akçaağaç",    "Kayısı",                       "Kermes meşesi",    "Kiraz",                "Kış tatlısı",
            "Kızılcık",                     "Kuş iğdesi",                   "Leylak",           "Mabet ağacı",          "Mandalina",
            "Manolya",                      "Mum çiçeği",                   "Nar",              "Osmanthus fragrans",   "Ova akcaağacı",
            "Oya ağacı",                    "Parlak yapraklı kurtbağrı",    "Pırnal mese",      "Salkım söğüt",         "Şeftali",
            "Süs eriği",                    "Süs kirazı",                   "Titrek kavak",     "Tüylü meşe",           "Üç dişli akçaagaç",
            "Yıldız çalısı",                "Zakkum"
    };

    public static String[] leaves_en = {

            "Hornbeam",                     "Boxwood",                      "White poplar",     "Laurel",               "Hawthorn",
            "Goldenrain tree",              "Sweet chestnut",               "Pear",             "Horse chestnut",       "Quince",
            "Almond",                       "White mulberry",               "Walnut Tree",      "Aesculus",             "Judas Tree",
            "Cinnamon",                     "Mulberry",                     "Sycamore maple",   "Ash",                  "Ivy",
            "Apple",                        "Judas tree",                   "Hazelnut",         "Lime trees",           "Common fig",
            "True indigo",                  "Japanese maple",               "Camphorwood",      "Black poplar",         "Cherry laurel",
            "Italian maple",                "Apricot",                      "Kermes oak",       "Cherry",               "Winter dessert",
            "Cornelian cherry",             "Wild olive",                   "Lilac",            "Maidenhair tree",      "Mandarine",
            "Magnolia",                     "Wax plant",                    "Pomegranate",      "Osmanthus fragrans",   "Field maple",
            "Crape myrtle",                 "Glossy privet",                "Evergreen oak",    "Babylon willow",       "Peach",
            "Cherry plum",                  "Hill cherry",                  "Common aspen",     "Downy oak",            "Trident maple",
            "Australian laurel",            "Oleander"
    };

    public static String[] latinnames = {
            "Carpinus betulus",             "Buxus sempervirens",           "Populus alba",         "Laurus nobilis",           "Crataegus monogyna jacq",
            "Koelreuteria paniculata laxm", "Castanea sativa mill",         "Pyrus communis",       "Aesculus hippocastanum",   "Cydonia oblonga mill",
            "Prunus dulcis",                "Morus alba",                   "Juglans regia",        "Aesculus chinensis",       "Cercis chinensis",
            "Cinnamomum japonicum sieb",    "Morus Platanifolia",           "Acer Pseudoplanatus",  "Fraxinus Excelsior",       "Hedera helix",
            "Malus domestica ",             "Cercis siliquastrum",          "Corylus avellana",     "Tilia cordata",            "Ficus carica",
            "Indigofera tinctoria",         "Acer palmatum",                "Cinnamomum camphora",  "Populus nigra",            "Prunus laurocerasus",
            "Acer opalus mill",             "Prunus armeniaca",             "Quercus coccifera",    "Prunus avium",             "Chimonantus praecox",
            "Cornus mas",                   "Elaeagnus angustifolia",       "Syringa vulgaris",     "Ginkgo biloba",            "Citrus reticulata blanco",
            "Magnolia grandiflora",         "Hoya carnosa",                 "Punica granatum",      "Osmanthus fragrans lour",  "Acer campestre",
            "Lagerstroemia indica",         "Ligustrum lucidum",            "Quercus ilex",         "Salix babylonica",         "Prunus persica batsch",
            "Prunus cerasifera",            "Prunus serrulata lindl",       "Populus tremula",      "Quercus pubescens willd",  "Acer buergerianum",
            "Pittosporum tobira",           "Nerium oleander"
    };

    public static int[] leaveint = {
            R.drawable.leaf_adi_gurgen,                 R.drawable.leaf_adi_simsir,                 R.drawable.leaf_ak_kavak,
            R.drawable.leaf_akdeniz_defnesi,            R.drawable.leaf_alic,                       R.drawable.leaf_altuni_fener_agaci,
            R.drawable.leaf_anadolu_kestanesi,          R.drawable.leaf_armut,                      R.drawable.leaf_at_kestanesi,
            R.drawable.leaf_ayva,                       R.drawable.leaf_badem,                      R.drawable.leaf_beyaz_dut,
            R.drawable.leaf_ceviz,                      R.drawable.leaf_cin_at_kestanesi,           R.drawable.leaf_cin_erguvani,
            R.drawable.leaf_cin_tarcini,                R.drawable.leaf_cinar_yaprakli_dut,         R.drawable.leaf_dag_akcaagaci,
            R.drawable.leaf_disbudak,                   R.drawable.leaf_duvar_sarmasigi,            R.drawable.leaf_elma,
            R.drawable.leaf_erguvan,                    R.drawable.leaf_findik,                     R.drawable.leaf_ihlamur,
            R.drawable.leaf_incir,                      R.drawable.leaf_indigo_agaci,               R.drawable.leaf_japon_akcaagaci,
            R.drawable.leaf_kafur_agaci,                R.drawable.leaf_kara_kavak,                 R.drawable.leaf_karayemis,
            R.drawable.leaf_kartopu_yaprakli_akcaagac,  R.drawable.leaf_kayisi,                     R.drawable.leaf_kermes_mesesi,
            R.drawable.leaf_kiraz,                      R.drawable.leaf_kis_tatlisi,                R.drawable.leaf_kizilcik,
            R.drawable.leaf_kus_igdesi,                 R.drawable.leaf_leylak,                     R.drawable.leaf_mabet_agaci,
            R.drawable.leaf_mandalina,                  R.drawable.leaf_manolya,                    R.drawable.leaf_mum_cicegi,
            R.drawable.leaf_nar,                        R.drawable.leaf_osmanthus_fragrans,         R.drawable.leaf_ova_akcaagaci,
            R.drawable.leaf_oya_agaci,                  R.drawable.leaf_parlak_yaprakli_kurtbagri,  R.drawable.leaf_pirnal_mese,
            R.drawable.leaf_salkim_sogut,               R.drawable.leaf_seftali,                    R.drawable.leaf_sus_erigi,
            R.drawable.leaf_sus_kirazi,                 R.drawable.leaf_titrek_kavak,               R.drawable.leaf_tuylu_mese,
            R.drawable.leaf_uc_disli_akcaagac,          R.drawable.leaf_yildiz_calisi,              R.drawable.leaf_zakkum
    };

    public static int[] treeint = {
            R.drawable.tree_adi_gurgen,                 R.drawable.tree_adi_simsir,                 R.drawable.tree_ak_kavak,
            R.drawable.tree_akdeniz_defnesi,            R.drawable.tree_alic,                       R.drawable.tree_altuni_fener_agaci,
            R.drawable.tree_anadolu_kestanesi,          R.drawable.tree_armut,                      R.drawable.tree_at_kestanesi,
            R.drawable.tree_ayva,                       R.drawable.tree_badem,                      R.drawable.tree_beyaz_dut,
            R.drawable.tree_ceviz,                      R.drawable.tree_cin_at_kestanesi,           R.drawable.tree_cin_erguvani,
            R.drawable.tree_cin_tarcini,                R.drawable.tree_cinar_yaprakli_dut,         R.drawable.tree_dag_akcaagaci,
            R.drawable.tree_disbudak,                   R.drawable.tree_duvar_sarmasigi,            R.drawable.tree_elma,
            R.drawable.tree_erguvan,                    R.drawable.tree_findik,                     R.drawable.tree_ihlamur,
            R.drawable.tree_incir,                      R.drawable.tree_indigo_agaci,               R.drawable.tree_japon_akcaagaci,
            R.drawable.tree_kafur_agaci,                R.drawable.tree_kara_kavak,                 R.drawable.tree_karayemis,
            R.drawable.tree_kartopu_yaprakli_akcaagac,  R.drawable.tree_kayisi,                     R.drawable.tree_kermes_mesesi,
            R.drawable.tree_kiraz,                      R.drawable.tree_kis_tatlisi,                R.drawable.tree_kizilcik,
            R.drawable.tree_kus_igdesi,                 R.drawable.tree_leylak,                     R.drawable.tree_mabet_agaci,
            R.drawable.tree_mandalina,                  R.drawable.tree_manolya,                    R.drawable.tree_mum_cicegi,
            R.drawable.tree_nar,                        R.drawable.tree_osmanthus_fragrans,         R.drawable.tree_ova_akcaagaci,
            R.drawable.tree_oya_agaci,                  R.drawable.tree_parlak_yaprakli_kurtbagri,  R.drawable.tree_pirnal_mese,
            R.drawable.tree_salkim_sogut,               R.drawable.tree_seftali,                    R.drawable.tree_sus_erigi,
            R.drawable.tree_sus_kirazi,                 R.drawable.tree_titrek_kavak,               R.drawable.tree_tuylu_mese,
            R.drawable.tree_uc_disli_akcaagac,          R.drawable.tree_yildiz_calisi,              R.drawable.tree_zakkum
    };

    public static boolean connectionCheck(Activity activity)
    {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();

        return nInfo != null && nInfo.isConnected();
    }
}
