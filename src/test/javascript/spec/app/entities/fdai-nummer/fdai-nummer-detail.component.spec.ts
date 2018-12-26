/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GradingAppTestModule } from '../../../test.module';
import { FdaiNummerDetailComponent } from 'app/entities/fdai-nummer/fdai-nummer-detail.component';
import { FdaiNummer } from 'app/shared/model/fdai-nummer.model';

describe('Component Tests', () => {
    describe('FdaiNummer Management Detail Component', () => {
        let comp: FdaiNummerDetailComponent;
        let fixture: ComponentFixture<FdaiNummerDetailComponent>;
        const route = ({ data: of({ fdaiNummer: new FdaiNummer(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GradingAppTestModule],
                declarations: [FdaiNummerDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FdaiNummerDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FdaiNummerDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.fdaiNummer).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
