package com.webbeaga.sistema.config;

import com.webbeaga.sistema.entity.*;
import com.webbeaga.sistema.repository.*;
import java.time.LocalDateTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @PersistenceContext
    private EntityManager em;

    private final UsuarioRepository usuarioRepo;
    private final RedeRepository redeRepo;
    private final PostoRepository postoRepo;
    private final RegistroPontoRepository pontoRepo;
    private final EventoRepository eventoRepo;
    private final LinkRepository linkRepo;
    private final PasswordEncoder encoder;
    private final DataSource dataSource;

    public DataInitializer(UsuarioRepository ur, RedeRepository rr, PostoRepository pr,
                           RegistroPontoRepository por, EventoRepository er, LinkRepository lr,
                           PasswordEncoder enc, DataSource ds) {
        this.usuarioRepo = ur;
        this.redeRepo    = rr;
        this.postoRepo   = pr;
        this.pontoRepo   = por;
        this.eventoRepo  = er;
        this.linkRepo    = lr;
        this.encoder     = enc;
        this.dataSource  = ds;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Fix CHECK constraint using raw JDBC (independent connection/transaction)
        fixTipoConstraint();

        boolean jaTemUsuariosNovos = usuarioRepo.findByUsername("thomasalexandre").isPresent();
        if (jaTemUsuariosNovos) {
            log.info("Banco ja populado — pulando seed.");
            return;
        }
        if (usuarioRepo.count() > 0) {
            log.info("Limpando dados antigos para re-seed...");
            pontoRepo.deleteAll();
            eventoRepo.deleteAll();
            postoRepo.deleteAll();
            redeRepo.deleteAll();
            usuarioRepo.deleteAll();
            em.flush();
            em.clear();
        }
        log.info("Inicializando banco de dados...");
        seedUsuarios();
        seedRedesPostos();
        seedPontos();
        seedLinks();
        log.info("Banco inicializado com sucesso!");
    }

    private void fixTipoConstraint() {
        try (Connection conn = dataSource.getConnection();
             Statement s = conn.createStatement()) {
            conn.setAutoCommit(false);
            s.execute("DELETE FROM eventos WHERE tipo NOT IN ('IMPLANTACAO','INSTALACAO_PDV','CO_WORK','TREINAMENTO')");
            s.execute("ALTER TABLE eventos DROP CONSTRAINT IF EXISTS eventos_tipo_check");
            s.execute("ALTER TABLE eventos ADD CONSTRAINT eventos_tipo_check CHECK (tipo IN ('IMPLANTACAO','INSTALACAO_PDV','CO_WORK','TREINAMENTO'))");
            // chamados
            s.execute("DELETE FROM chamados WHERE tipo NOT IN ('PDV','RETAGUARDA','INSTALACAO_PDV','MAQUININHA','SPED','IMPRESSORA','CERTIFICADO')");
            s.execute("ALTER TABLE chamados DROP CONSTRAINT IF EXISTS chamados_tipo_check");
            s.execute("ALTER TABLE chamados ADD CONSTRAINT chamados_tipo_check CHECK (tipo IN ('PDV','RETAGUARDA','INSTALACAO_PDV','MAQUININHA','SPED','IMPRESSORA','CERTIFICADO'))");
            conn.commit();
            log.info("Constraints de tipo atualizadas.");
        } catch (Exception e) {
            log.warn("Nao foi possivel atualizar constraint: {}", e.getMessage());
        }
    }

    private void seedUsuarios() {
        List<Usuario> lista = new ArrayList<>();

        // ── Administradores ──────────────────────────────────────────────
        lista.add(criarUsuario("admin",           "admin123",  "Admin",              "Administrador",    Usuario.Role.ADMIN));
        lista.add(criarUsuario("joaohenrique",    "123@WEBBH", "Joao Henrique",      "Administrador",    Usuario.Role.ADMIN));
        lista.add(criarUsuario("mateuslemos",     "123@WEBBH", "Mateus Lemos",       "Administrador",    Usuario.Role.ADMIN));

        // ── Funcionários ─────────────────────────────────────────────────
        lista.add(criarUsuario("viniciusparanho", "123@WEBBH", "Vinicius Paranho",   "Analista de Suporte", Usuario.Role.USER));
        lista.add(criarUsuario("samuelalonso",    "123@WEBBH", "Samuel Alonso",      "Analista de Suporte", Usuario.Role.USER));
        lista.add(criarUsuario("danielduarte",    "123@WEBBH", "Daniel Duarte",      "Analista de Suporte", Usuario.Role.USER));
        lista.add(criarUsuario("douglascosta",    "123@WEBBH", "Douglas Costa",      "Analista de Suporte", Usuario.Role.USER));
        lista.add(criarUsuario("wesleyaraujo",    "123@WEBBH", "Wesley Araujo",      "Analista de Suporte", Usuario.Role.USER));
        lista.add(criarUsuario("pedrozeferino",   "123@WEBBH", "Pedro Zeferino",     "Analista de Suporte", Usuario.Role.USER));
        lista.add(criarUsuario("keniasoraia",     "123@WEBBH", "Kenia Soraia",       "Analista de Suporte", Usuario.Role.USER));
        lista.add(criarUsuario("thomasalexandre","123@WEBBH", "Thomas Alexandre",   "Analista de Suporte", Usuario.Role.USER));

        usuarioRepo.saveAll(lista);
        log.info("{} usuarios criados.", lista.size());
    }

    private Usuario criarUsuario(String username, String senha, String nome, String cargo, Usuario.Role role) {
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setPassword(encoder.encode(senha));
        u.setNomeCompleto(nome);
        u.setCargo(cargo);
        u.setRole(role);
        u.setEmail(username + "@webbeaga.com.br");
        u.setTelefone("");
        u.setCargaHorariaDiaria(8);
        return u;
    }

    private void seedRedesPostos() {
        Rede ip = new Rede();
        ip.setNome("Rede Ipiranga"); ip.setBandeira("Ipiranga");
        ip = redeRepo.save(ip);

        Rede sh = new Rede();
        sh.setNome("Rede Shell"); sh.setBandeira("Shell");
        sh = redeRepo.save(sh);

        Rede br = new Rede();
        br.setNome("Rede BR / Petrobras"); br.setBandeira("BR");
        br = redeRepo.save(br);

        Rede rz = new Rede();
        rz.setNome("Rede Raizen"); rz.setBandeira("Raizen");
        rz = redeRepo.save(rz);

        List<Posto> postos = new ArrayList<>();
        postos.add(criarPosto("Posto Ipiranga Centro",   "Av. Amazonas, 1200",       "Belo Horizonte", "123 456 789", true,  ip));
        postos.add(criarPosto("Posto Ipiranga Savassi",  "Rua Pernambuco, 450",      "Belo Horizonte", "456 789 012", true,  ip));
        postos.add(criarPosto("Posto Ipiranga Barreiro", "Av. Perimetral, 780",      "Belo Horizonte", "789 012 345", false, ip));
        postos.add(criarPosto("Auto Posto Vitoria",      "Av. do Contorno, 3400",    "Belo Horizonte", "321 654 987", true,  sh));
        postos.add(criarPosto("Shell Venda Nova",        "R. Pe. Pedro Pinto, 1800", "Belo Horizonte", "654 987 321", true,  sh));
        postos.add(criarPosto("Posto Bandeirantes",      "Av. Bandeirantes, 2100",   "Belo Horizonte", "111 222 333", true,  br));
        postos.add(criarPosto("BR Pampulha",             "Rua Prof. Morais, 965",    "Belo Horizonte", "444 555 666", false, br));
        postos.add(criarPosto("BR Contagem",             "Av. Joao Cesar, 2200",     "Contagem",       "999 888 777", true,  br));
        postos.add(criarPosto("Posto Nova Lima",         "Rod. MG-030, km 12",       "Nova Lima",      "777 888 999", true,  rz));
        postos.add(criarPosto("Raizen Contagem",         "Av. Joao Cesar, 1550",     "Contagem",       "000 111 222", true,  rz));
        postoRepo.saveAll(postos);
        log.info("4 redes e 10 postos criados.");
    }

    private Posto criarPosto(String nome, String end, String cidade, String desk, boolean online, Rede rede) {
        Posto p = new Posto();
        p.setNome(nome); p.setEndereco(end); p.setCidade(cidade);
        p.setAnyDeskId(desk); p.setOnline(online); p.setRede(rede);
        return p;
    }

    private void seedPontos() {
        Usuario vinicius = usuarioRepo.findByUsername("viniciusparanho").orElseThrow();
        Usuario samuel   = usuarioRepo.findByUsername("samuelalonso").orElseThrow();
        Usuario daniel   = usuarioRepo.findByUsername("danielduarte").orElseThrow();
        LocalDate hoje   = LocalDate.now();

        salvarPonto(vinicius, hoje.minusDays(1), "08:00","12:05","13:05","17:58");
        salvarPonto(vinicius, hoje.minusDays(2), "07:55","12:00","13:00","18:10");
        salvarPonto(vinicius, hoje.minusDays(5), "08:10","12:15","13:15","17:45");
        salvarPonto(samuel,   hoje.minusDays(1), "08:30","12:30","13:30","18:30");
        salvarPonto(samuel,   hoje.minusDays(2), "08:00","12:00","13:00","18:00");
        salvarPonto(samuel,   hoje.minusDays(5), "08:00","12:00","13:00","20:00");
        salvarPonto(daniel,   hoje.minusDays(1), "08:00","12:00","13:00","17:00");
        salvarPonto(daniel,   hoje.minusDays(2), "08:05","12:05","13:05","17:05");

        log.info("Historico de pontos criado.");
    }

    private void seedLinks() {
        if (linkRepo.count() > 0) return;
        Usuario admin = usuarioRepo.findByUsername("admin").orElseThrow();
        String[][] links = {
            {"Site Parceiros",          "https://www.qualityautomacao.com.br/parceiro/#/home"},
            {"Site Arquivos Quality",   "http://arquivo.qualityautomacao.com.br/"},
            {"Site Confluence",         "https://qualityautomacao.atlassian.net/wiki/spaces/webPosto/overview"},
            {"Site Pay",                "https://qualityautomacao.com.br/config-pay/"},
            {"Site Sefaz",              "https://monitor.tecnospeed.com.br/?&filter-uf=mg"},
            {"Site Pagpix",             "https://maispagamentos.statuspage.io/"},
            {"Link Instalações PDV",    "https://cursos.nutror.com/curso/13bfe01f0653ec41fd12c30cb45d9c0352d5bbfa"},
            {"Link Rotinas Básicas",    "https://cursos.nutror.com/curso/cb44c05c870de7447248244184f2e23bca9108fa"},
            {"Link WebPosto Avançado",  "https://cursos.nutror.com/curso/2a010b3a571891e134fdfd8d2c156f433f499db0"},
            {"Link Config Pista",       "https://vieiraphv.github.io/senhapay/"},
            {"Inscrição Estadual",      "http://www.sintegra.gov.br/"}
        };
        for (String[] entry : links) {
            Link l = new Link();
            l.setTitulo(entry[0]);
            l.setUrl(entry[1]);
            l.setCriadoPor(admin);
            l.setCriadoEm(LocalDateTime.now());
            linkRepo.save(l);
        }
        log.info("Links uteis criados.");
    }

    private void salvarPonto(Usuario u, LocalDate data, String e, String sa, String ra, String s) {
        RegistroPonto p = new RegistroPonto();
        p.setUsuario(u);
        p.setData(data);
        p.setHoraEntrada(LocalTime.parse(e));
        p.setHoraSaidaAlmoco(LocalTime.parse(sa));
        p.setHoraRetornoAlmoco(LocalTime.parse(ra));
        p.setHoraSaida(LocalTime.parse(s));
        p.recalcular();
        pontoRepo.save(p);
    }
}
