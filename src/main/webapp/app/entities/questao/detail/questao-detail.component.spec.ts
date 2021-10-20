import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuestaoDetailComponent } from './questao-detail.component';

describe('Questao Management Detail Component', () => {
  let comp: QuestaoDetailComponent;
  let fixture: ComponentFixture<QuestaoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuestaoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ questao: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(QuestaoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(QuestaoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load questao on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.questao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
