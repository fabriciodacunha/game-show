jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RespostaService } from '../service/resposta.service';
import { IResposta, Resposta } from '../resposta.model';
import { IQuestao } from 'app/entities/questao/questao.model';
import { QuestaoService } from 'app/entities/questao/service/questao.service';
import { IParticipante } from 'app/entities/participante/participante.model';
import { ParticipanteService } from 'app/entities/participante/service/participante.service';
import { IPlay } from 'app/entities/play/play.model';
import { PlayService } from 'app/entities/play/service/play.service';

import { RespostaUpdateComponent } from './resposta-update.component';

describe('Resposta Management Update Component', () => {
  let comp: RespostaUpdateComponent;
  let fixture: ComponentFixture<RespostaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let respostaService: RespostaService;
  let questaoService: QuestaoService;
  let participanteService: ParticipanteService;
  let playService: PlayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RespostaUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RespostaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RespostaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    respostaService = TestBed.inject(RespostaService);
    questaoService = TestBed.inject(QuestaoService);
    participanteService = TestBed.inject(ParticipanteService);
    playService = TestBed.inject(PlayService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Questao query and add missing value', () => {
      const resposta: IResposta = { id: 456 };
      const questao: IQuestao = { id: 24948 };
      resposta.questao = questao;

      const questaoCollection: IQuestao[] = [{ id: 62048 }];
      jest.spyOn(questaoService, 'query').mockReturnValue(of(new HttpResponse({ body: questaoCollection })));
      const additionalQuestaos = [questao];
      const expectedCollection: IQuestao[] = [...additionalQuestaos, ...questaoCollection];
      jest.spyOn(questaoService, 'addQuestaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resposta });
      comp.ngOnInit();

      expect(questaoService.query).toHaveBeenCalled();
      expect(questaoService.addQuestaoToCollectionIfMissing).toHaveBeenCalledWith(questaoCollection, ...additionalQuestaos);
      expect(comp.questaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Participante query and add missing value', () => {
      const resposta: IResposta = { id: 456 };
      const participante: IParticipante = { id: 29406 };
      resposta.participante = participante;

      const participanteCollection: IParticipante[] = [{ id: 50964 }];
      jest.spyOn(participanteService, 'query').mockReturnValue(of(new HttpResponse({ body: participanteCollection })));
      const additionalParticipantes = [participante];
      const expectedCollection: IParticipante[] = [...additionalParticipantes, ...participanteCollection];
      jest.spyOn(participanteService, 'addParticipanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resposta });
      comp.ngOnInit();

      expect(participanteService.query).toHaveBeenCalled();
      expect(participanteService.addParticipanteToCollectionIfMissing).toHaveBeenCalledWith(
        participanteCollection,
        ...additionalParticipantes
      );
      expect(comp.participantesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Play query and add missing value', () => {
      const resposta: IResposta = { id: 456 };
      const play: IPlay = { id: 40835 };
      resposta.play = play;

      const playCollection: IPlay[] = [{ id: 271 }];
      jest.spyOn(playService, 'query').mockReturnValue(of(new HttpResponse({ body: playCollection })));
      const additionalPlays = [play];
      const expectedCollection: IPlay[] = [...additionalPlays, ...playCollection];
      jest.spyOn(playService, 'addPlayToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resposta });
      comp.ngOnInit();

      expect(playService.query).toHaveBeenCalled();
      expect(playService.addPlayToCollectionIfMissing).toHaveBeenCalledWith(playCollection, ...additionalPlays);
      expect(comp.playsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const resposta: IResposta = { id: 456 };
      const questao: IQuestao = { id: 47592 };
      resposta.questao = questao;
      const participante: IParticipante = { id: 37028 };
      resposta.participante = participante;
      const play: IPlay = { id: 68057 };
      resposta.play = play;

      activatedRoute.data = of({ resposta });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(resposta));
      expect(comp.questaosSharedCollection).toContain(questao);
      expect(comp.participantesSharedCollection).toContain(participante);
      expect(comp.playsSharedCollection).toContain(play);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Resposta>>();
      const resposta = { id: 123 };
      jest.spyOn(respostaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resposta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resposta }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(respostaService.update).toHaveBeenCalledWith(resposta);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Resposta>>();
      const resposta = new Resposta();
      jest.spyOn(respostaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resposta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resposta }));
      saveSubject.complete();

      // THEN
      expect(respostaService.create).toHaveBeenCalledWith(resposta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Resposta>>();
      const resposta = { id: 123 };
      jest.spyOn(respostaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resposta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(respostaService.update).toHaveBeenCalledWith(resposta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackQuestaoById', () => {
      it('Should return tracked Questao primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuestaoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackParticipanteById', () => {
      it('Should return tracked Participante primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackParticipanteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPlayById', () => {
      it('Should return tracked Play primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlayById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
