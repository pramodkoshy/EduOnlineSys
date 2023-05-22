import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EduOnlineTestModule } from '../../../test.module';
import { AnswerItemDetailComponent } from 'app/entities/answer-item/answer-item-detail.component';
import { AnswerItem } from 'app/shared/model/answer-item.model';

describe('Component Tests', () => {
  describe('AnswerItem Management Detail Component', () => {
    let comp: AnswerItemDetailComponent;
    let fixture: ComponentFixture<AnswerItemDetailComponent>;
    const route = ({ data: of({ answerItem: new AnswerItem(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [AnswerItemDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AnswerItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AnswerItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load answerItem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.answerItem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
